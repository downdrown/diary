package at.downdrown.diary.frontend

import at.downdrown.diary.frontend.extensions.i18n
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class Notifications {
    companion object {
        fun showSaveSuccess() {
            successNotification("notification.save.success".i18n()).open()
        }

        fun showRegistrationSuccess(registeredUsername: String) {
            successNotification("notification.registration.success".i18n(registeredUsername)).open()
        }

        fun showChangePasswordSuccess() {
            successNotification("notification.changepassword.success".i18n()).open()
        }

        fun showDateSelectionSuccess(selectedDate: LocalDate) {
            val currentLocale = UI.getCurrent().locale
            val localizedFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(currentLocale)
            successNotification(
                "notification.dateselection.success".i18n(selectedDate.format(localizedFormatter))
            ).open()
        }

        private fun successNotification(notification: String, vararg params: Any): Notification {

            val notificationComponent = Notification()
            notificationComponent.duration = 5_000
            notificationComponent.position = Notification.Position.BOTTOM_END

            val notificationLabel = Label(notification)

            val dismissButton = Button("notification.button.dismiss".i18n(params)) { notificationComponent.close() }
            dismissButton.addThemeVariants(ButtonVariant.LUMO_SMALL)
            dismissButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)

            val layout = HorizontalLayout(
                notificationLabel,
                dismissButton
            )
            layout.setMinWidth(200f, Unit.PIXELS)
            layout.expand(notificationLabel)
            layout.alignItems = FlexComponent.Alignment.CENTER

            notificationComponent.add(layout)
            return notificationComponent
        }
    }
}
