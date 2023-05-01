package at.downdrown.diary.frontend.view

import at.downdrown.diary.api.mood.MoodCheckInService
import at.downdrown.diary.api.security.userPrincipal
import at.downdrown.diary.frontend.Notifications
import at.downdrown.diary.frontend.component.Banner
import at.downdrown.diary.frontend.dialog.MoodCheckInDialog
import at.downdrown.diary.frontend.event.DateSelectedEvent
import at.downdrown.diary.frontend.event.YearMonthSelectedEvent
import at.downdrown.diary.frontend.event.onEvent
import at.downdrown.diary.frontend.extensions.i18n
import at.downdrown.diary.frontend.extensions.isDesktop
import at.downdrown.diary.frontend.layout.GridLayout
import at.downdrown.diary.frontend.layout.MainLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route
import jakarta.annotation.security.PermitAll
import mu.KotlinLogging
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private val log = KotlinLogging.logger {}

@Route("/", layout = MainLayout::class)
@PermitAll
@CssImport("./styles.css")
class TodayView(
    private val moodCheckInService: MoodCheckInService
) : GridLayout(), HasDynamicTitle, BeforeEnterObserver {

    init {

        onEvent(DateSelectedEvent::class.java) { event -> handleDateSelection(event.value) }
        onEvent(YearMonthSelectedEvent::class.java) { event -> handleYearMonthSelection(event.value) }

        addExpanded(greeting())
        add(checkInAction())
    }

    override fun getPageTitle(): String {
        return "main.pagetitle".i18n()
    }

    private fun handleDateSelection(selectedDate: LocalDate) {
        log.trace { "The user selected a new date: ${selectedDate.format(DateTimeFormatter.ISO_DATE)}" }
        if (isDesktop()) Notifications.showDateSelectionSuccess(selectedDate)
    }

    private fun handleYearMonthSelection(selectedYearMonth: YearMonth) {
        log.trace {
            "The user selected a new year-month combination: " +
                    selectedYearMonth.format(DateTimeFormatter.ofPattern("MMM yyyy"))
        }
    }

    private fun greeting(): Component {
        return Banner(
            "today.greeting.heading".i18n(userPrincipal().fullname),
            "today.greeting.content".i18n()
        )
    }

    private fun checkInAction(): Component {
        val button = Button("today.action.addmoodcheckin".i18n()) {
            MoodCheckInDialog(true, moodCheckInService).open()
        }
        button.icon = VaadinIcon.USER_HEART.create()
        button.addThemeVariants(ButtonVariant.LUMO_LARGE)
        button.setWidthFull()
        button.setHeight(80f, Unit.PIXELS)
        return button
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        showMoodCheckInAtLeastOnceADay()
    }

    private fun showMoodCheckInAtLeastOnceADay() {
        if (!moodCheckInService.hasAlreadyCheckedIn(userPrincipal().username, LocalDate.now())) {
            MoodCheckInDialog(false, moodCheckInService).open()
        }
    }
}
