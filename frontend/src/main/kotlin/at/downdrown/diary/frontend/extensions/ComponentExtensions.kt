package at.downdrown.diary.frontend.extensions

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasEnabled
fun HasEnabled.enable() {
    isEnabled = true
}

fun HasEnabled.disable() {
    isEnabled = false
}

fun Component.hide() {
    isVisible = false
}

fun Component.show() {
    isVisible = true
}