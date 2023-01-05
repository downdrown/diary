package at.downdrown.diary.frontend.extensions

import com.vaadin.flow.component.textfield.TextField

fun TextField.colorTextRed() {
    style.set("color", "var(--lumo-error-text-color)")
}
fun TextField.colorTextGreen() {
    style.set("color", "var(--lumo-success-text-color)")
}