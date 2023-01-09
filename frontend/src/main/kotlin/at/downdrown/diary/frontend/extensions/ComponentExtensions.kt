package at.downdrown.diary.frontend.extensions

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasEnabled
import com.vaadin.flow.component.html.Span

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

fun Span.colorTextRed() {
    style.set("color", "var(--lumo-error-text-color)")
}

fun Span.colorTextYellow() {
    style.set("color", "var(--lumo-warning-text-color)")
}

fun Span.colorTextGreen() {
    style.set("color", "var(--lumo-success-text-color)")
}
