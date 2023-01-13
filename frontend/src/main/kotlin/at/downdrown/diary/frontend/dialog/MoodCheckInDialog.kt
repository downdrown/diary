package at.downdrown.diary.frontend.dialog

import at.downdrown.diary.api.mood.MoodCheckIn
import at.downdrown.diary.api.mood.MoodCheckInService
import at.downdrown.diary.api.mood.MoodScore
import at.downdrown.diary.frontend.Notifications
import at.downdrown.diary.frontend.conversion.Converters
import at.downdrown.diary.frontend.extensions.hasValidChanges
import at.downdrown.diary.frontend.extensions.hide
import at.downdrown.diary.frontend.extensions.i18n
import at.downdrown.diary.frontend.extensions.show
import at.downdrown.diary.frontend.validation.Validators
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.listbox.ListBox
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.Validator
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.value.ValueChangeMode
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class MoodCheckInDialog(
    private val allowCheckInPointSelection: Boolean,
    private val moodCheckInService: MoodCheckInService
) : Dialog() {

    private val binder = Binder(MoodCheckInFormModel::class.java)
    private val now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS)
    private val moodCheckIn = MoodCheckInFormModel(now)

    // hacky workaround for https://github.com/vaadin/vaadin-list-box/issues/19
    private var moodScoreHasValue: () -> Boolean = { false }

    init {

        headerTitle = i18n("moodcheckin.headertitle")
        isCloseOnEsc = false
        isCloseOnOutsideClick = false

        setMinWidth(250f, Unit.PIXELS)
        setMaxWidth(400f, Unit.PIXELS)
        setMaxHeight(80f, Unit.PERCENTAGE)

        add(form())

        val saveButton = Button(i18n("moodcheckin.submit")) { handleSave() }
        saveButton.addThemeVariants(ButtonVariant.LUMO_SMALL)
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        saveButton.hide()

        val closeButton = Button(i18n("moodcheckin.cancel")) { handleClose() }
        closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL)

        footer.add(closeButton, saveButton)

        binder.addValueChangeListener {
            if (binder.hasValidChanges() && moodScoreHasValue()) {
                saveButton.show()
            } else {
                saveButton.hide()
            }
        }
        binder.readBean(moodCheckIn)

    }

    private fun form(): FormLayout {

        val moodScore = ListBox<MoodScore>()
        moodScoreHasValue = { moodScore.value != null }
        moodScore.setItems(*MoodScore.values())
        moodScore.addClassName("noselector")
        moodScore.addValueChangeListener { event -> event.source.listDataView.addFilter { it == event.value } }
        moodScore.setRenderer(ComponentRenderer() { item ->

            val label = item.label()
            label.style.set("font-size", "var(--lumo-font-size-xl)")

            val layout = HorizontalLayout(
                item.icon(),
                label
            )
            layout.alignItems = FlexComponent.Alignment.CENTER
            layout.isSpacing = true
            layout
        })


        val checkInPoint = DateTimePicker(i18n("moodcheckin.form.checkinpoint"))
        checkInPoint.isVisible = allowCheckInPointSelection

        val comment = TextArea()
        comment.hide()
        comment.placeholder = i18n("moodcheckin.form.comment.placeholder")
        comment.maxLength = 5000
        comment.valueChangeTimeout = 200
        comment.valueChangeMode = ValueChangeMode.LAZY
        comment.setMaxHeight(200f, Unit.PIXELS)

        val characterCountLabel = Label("${comment.value.length} / ${comment.maxLength}")
        characterCountLabel.style
            .set("color", "var(--lumo-contrast-80pct)")
            .set("font-size", "var(--lumo-font-size-xxs)")
            .set("line-height", "var(--lumo-line-height-xs)")

        comment.helperComponent = characterCountLabel
        comment.addValueChangeListener { characterCountLabel.text = "${comment.value.length} / ${comment.maxLength}" }

        val addCommentButton = Button(i18n("moodcheckin.form.addcomment")) { e -> comment.show(); e.source.hide() }
        addCommentButton.addThemeVariants(ButtonVariant.LUMO_SMALL)
        addCommentButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)

        val formLayout = FormLayout(
            moodScore,
            checkInPoint,
            addCommentButton,
            comment
        )

        formLayout.setResponsiveSteps(FormLayout.ResponsiveStep("0", 1))
        formLayout.setWidthFull()

        binder.forField(moodScore)
            .bind(MoodCheckInFormModel::score.getter, MoodCheckInFormModel::score.setter)

        binder.forField(checkInPoint)
            .withValidator(if (allowCheckInPointSelection) Validators.dateTimeInPast(now) else Validator.alwaysPass())
            .bind(MoodCheckInFormModel::checkInPoint.getter, MoodCheckInFormModel::checkInPoint.setter)

        binder.forField(comment)
            .withConverter(Converters.emptyStringToNullConverter())
            .bind(MoodCheckInFormModel::comment.getter, MoodCheckInFormModel::comment.setter)

        return formLayout
    }

    private fun handleSave() {

        binder.writeBeanIfValid(moodCheckIn)
        moodCheckInService.checkIn(moodCheckIn.toApi())
        Notifications.showSaveSuccess()

        close()
    }

    private fun handleClose() {
        if (binder.hasValidChanges()) {

            val confirmDialog = ConfirmDialog()
            confirmDialog.isCloseOnEsc = false
            confirmDialog.setHeader(i18n("moodcheckin.cancel.unsaved.header"))
            confirmDialog.setText(i18n("moodcheckin.cancel.unsaved.text"))
            confirmDialog.setConfirmText(i18n("moodcheckin.submit"))
            confirmDialog.setCancelable(true)
            confirmDialog.setCancelText(i18n("moodcheckin.cancel"))
            confirmDialog.setRejectable(true)
            confirmDialog.setRejectText(i18n("moodcheckin.cancel.unsaved.discard"))
            confirmDialog.addCancelListener { confirmDialog.close() }
            confirmDialog.addRejectListener { confirmDialog.close(); close() }
            confirmDialog.addConfirmListener { handleSave() }
            confirmDialog.open()

        } else {
            close()
        }
    }

    data class MoodCheckInFormModel(
        var score: MoodScore?,
        var checkInPoint: LocalDateTime?,
        var comment: String?
    ) {
        constructor(checkInPoint: LocalDateTime) : this(
            null,
            checkInPoint,
            null
        )

        fun toApi(): MoodCheckIn {
            if (score == null || checkInPoint == null) {
                throw IllegalArgumentException("required fields not set")
            }
            return MoodCheckIn(
                null,
                score!!,
                checkInPoint!!,
                comment,
                null
            )
        }
    }
}
