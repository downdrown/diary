package at.downdrown.diary.frontend

import at.downdrown.diary.frontend.extensions.i18n
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class Notifications {
    companion object {
        fun showSaveSuccess() {

            val notification = Notification()
            notification.duration = 5_000
            notification.position = Notification.Position.BOTTOM_END

            val notificationLabel = Label(i18n("notification.save.success"))

            val dismissButton = Button(i18n("notification.button.dismiss")) { notification.close() }
            dismissButton.addThemeVariants(ButtonVariant.LUMO_SMALL)
            dismissButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)

            val layout = HorizontalLayout(
                notificationLabel,
                dismissButton
            )
            layout.setMinWidth(200f, Unit.PIXELS)
            layout.expand(notificationLabel)
            layout.alignItems = FlexComponent.Alignment.CENTER

            notification.add(layout)
            notification.open()
        }
    }
}
