package at.downdrown.diary.frontend.dialog

import at.downdrown.diary.api.security.userPrincipal
import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.frontend.Notifications
import at.downdrown.diary.frontend.extensions.*
import at.downdrown.diary.frontend.validation.Validators
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.data.binder.Binder

class ChangePasswordDialog(
    private val validators: Validators,
    private val userService: UserService
) : Dialog() {

    private val binder = Binder(ChangePasswordModel::class.java)
    private val changePasswordModel = ChangePasswordModel(null, null, null)

    init {

        isCloseOnEsc = false
        isCloseOnOutsideClick = false

        setMaxWidth(400f, Unit.PIXELS)

        add(form())

        val saveButton = Button("changepassword.submit".i18n()) { handleSave() }
        saveButton.addThemeVariants(ButtonVariant.LUMO_SMALL)
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        saveButton.hide()

        val closeButton = Button("changepassword.cancel".i18n()) { handleClose() }
        closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL)

        footer.add(closeButton, saveButton)

        binder.addValueChangeListener { if (binder.hasValidChanges()) saveButton.show() else saveButton.hide() }
        binder.readBean(changePasswordModel)
    }

    private fun form(): FormLayout {

        val currentPassword = PasswordField("changepassword.form.currentpassword".i18n())
        val newPassword = PasswordField("changepassword.form.newpassword".i18n())
        validators.addDefaultHelperComponentFor(newPassword)
        val confirmNewPassword = PasswordField("changepassword.form.confirmnewpassword".i18n())

        binder.forField(currentPassword)
            .withValidator(validators.passwordMatchesCurrent())
            .asRequired()
            .bind(ChangePasswordModel::currentPassword.getter, ChangePasswordModel::currentPassword.setter)

        binder.forField(newPassword)
            .asRequired()
            .withValidator(validators.passwordDoesntMatchCurrent())
            .withValidator(validators.passwordEquals(confirmNewPassword))
            .bind(ChangePasswordModel::newPassword.getter, ChangePasswordModel::newPassword.setter)

        binder.forField(confirmNewPassword)
            .asRequired()
            .withValidator(validators.passwordEquals(newPassword))
            .bind(ChangePasswordModel::confirmNewPassword.getter, ChangePasswordModel::confirmNewPassword.setter)

        return FormLayout(
            currentPassword,
            newPassword,
            confirmNewPassword
        )
    }


    private fun handleSave() {

        binder.writeBeanIfValid(changePasswordModel)
        userService.changePassword(
            userPrincipal().username,
            changePasswordModel.currentPassword!!,
            changePasswordModel.newPassword!!
        )
        Notifications.showChangePasswordSuccess()

        close()
    }

    private fun handleClose() {
        if (binder.hasValidChanges()) {

            val confirmDialog = ConfirmDialog()
            confirmDialog.isCloseOnEsc = false
            confirmDialog.setHeader("changepassword.cancel.unsaved.header".i18n())
            confirmDialog.setText("changepassword.cancel.unsaved.text".i18n())
            confirmDialog.setConfirmText("changepassword.submit".i18n())
            confirmDialog.setCancelable(true)
            confirmDialog.setCancelText("changepassword.cancel".i18n())
            confirmDialog.setRejectable(true)
            confirmDialog.setRejectText("changepassword.cancel.unsaved.discard".i18n())
            confirmDialog.addCancelListener { confirmDialog.close() }
            confirmDialog.addRejectListener { confirmDialog.close(); close() }
            confirmDialog.addConfirmListener { handleSave() }
            confirmDialog.open()

        } else {
            close()
        }
    }

    data class ChangePasswordModel(
        var currentPassword: String?,
        var newPassword: String?,
        var confirmNewPassword: String?
    )
}
