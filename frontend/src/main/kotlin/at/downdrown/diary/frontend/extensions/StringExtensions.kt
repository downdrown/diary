package at.downdrown.diary.frontend.extensions

import com.vaadin.flow.component.UI

fun String.i18n(vararg params: Any): String {
    return UI.getCurrent().getTranslation(this, *params)
}
