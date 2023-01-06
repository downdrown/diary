package at.downdrown.diary.frontend.dialog

import at.downdrown.diary.frontend.extensions.userPrincipal
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.avatar.AvatarVariant
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout

class ProfileDialog: Dialog() {

    private val userPrincipal = userPrincipal()

    init {
        val layout = VerticalLayout()
        layout.setMinWidth(400f, Unit.PIXELS)
        layout.setMinHeight(600f, Unit.PIXELS)
        layout.alignItems = FlexComponent.Alignment.CENTER

        val avatar = Avatar(userPrincipal.fullname)
        avatar.addThemeVariants(AvatarVariant.LUMO_LARGE)
        layout.add(avatar)

        add(layout)

        val closeButton = Button("Close") { close() }
        footer.add(closeButton)
    }

}