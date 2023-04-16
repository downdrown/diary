package at.downdrown.diary.frontend.dialog

import at.downdrown.diary.api.security.userPrincipal
import at.downdrown.diary.api.user.User
import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.frontend.Notifications
import at.downdrown.diary.frontend.extensions.*
import at.downdrown.diary.frontend.validation.Validators
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.avatar.AvatarVariant
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.EmailField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder

class ProfileDialog(
    private val validators: Validators,
    private val userService: UserService
) : Dialog() {

    private val userPrincipal = userPrincipal()
    private val binder = Binder(User::class.java)

    init {

        isCloseOnEsc = false
        isCloseOnOutsideClick = false

        setMinWidth(500f, Unit.PIXELS)
        setMaxWidth(700f, Unit.PIXELS)
        setMaxHeight(80f, Unit.PERCENTAGE)

        val avatar = Avatar(userPrincipal.fullname)
        avatar.addThemeVariants(AvatarVariant.LUMO_XLARGE)

        val layout = VerticalLayout(
            avatar,
            form()
        )
        layout.setWidthFull()
        layout.alignItems = FlexComponent.Alignment.CENTER

        add(layout)

        val saveButton = Button("profile.submit".i18n()) { handleSave() }
        saveButton.addThemeVariants(ButtonVariant.LUMO_SMALL)
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        saveButton.hide()

        val closeButton = Button("profile.cancel".i18n()) { handleClose() }
        closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL)

        footer.add(closeButton, saveButton)

        binder.addValueChangeListener { if (binder.hasValidChanges()) saveButton.show() else saveButton.hide() }
        binder.readBean(userPrincipal.user)
    }

    private fun form(): FormLayout {

        val username = TextField("profile.form.username".i18n(), "profile.form.username.placeholder".i18n())
        username.prefixComponent = VaadinIcon.AT.create()

        val firstname = TextField("profile.form.firstname".i18n())
        val lastname = TextField("profile.form.lastname".i18n())
        val birthdate = DatePicker("profile.form.birthdate".i18n())
        val email = EmailField("profile.form.email".i18n())

        val formLayout = FormLayout(
            username,
            firstname,
            lastname,
            birthdate,
            email
        )

        formLayout.setResponsiveSteps(
            FormLayout.ResponsiveStep("0", 1),
            FormLayout.ResponsiveStep("400px", 2)
        )

        formLayout.setColspan(username, 2)
        formLayout.setWidthFull()

        binder.forField(username)
            .asRequired()
            .withValidator(validators.usernameValidator())
            .bind(User::username.getter, User::username.setter)

        binder.forField(firstname)
            .asRequired()
            .bind(User::firstname.getter, User::firstname.setter)

        binder.forField(lastname)
            .asRequired()
            .bind(User::lastname.getter, User::lastname.setter)

        binder.forField(birthdate)
            .withValidator(validators.birthdateValidator())
            .bind(User::birthdate.getter, User::birthdate.setter)

        binder.forField(email)
            .withValidator(validators.emailValidator())
            .bind(User::email.getter, User::email.setter)

        return formLayout
    }

    private fun handleClose() {
        if (binder.hasValidChanges()) {

            val confirmDialog = ConfirmDialog()
            confirmDialog.isCloseOnEsc = false
            confirmDialog.setHeader("profile.cancel.unsaved.header".i18n())
            confirmDialog.setText("profile.cancel.unsaved.text".i18n())
            confirmDialog.setConfirmText("profile.submit".i18n())
            confirmDialog.setCancelable(true)
            confirmDialog.setCancelText("profile.cancel".i18n())
            confirmDialog.setRejectable(true)
            confirmDialog.setRejectText("profile.cancel.unsaved.discard".i18n())
            confirmDialog.addCancelListener { confirmDialog.close() }
            confirmDialog.addRejectListener { confirmDialog.close(); close() }
            confirmDialog.addConfirmListener { handleSave() }
            confirmDialog.open()

        } else {
            close()
        }
    }

    private fun handleSave() {

        val user = userPrincipal.user

        binder.writeBeanIfValid(user)
        userService.update(user)
        Notifications.showSaveSuccess()

        close()
    }
}
