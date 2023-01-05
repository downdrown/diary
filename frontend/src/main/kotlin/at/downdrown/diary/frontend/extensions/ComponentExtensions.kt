package at.downdrown.diary.frontend.extensions

import com.vaadin.flow.component.Component

fun Component.i18n(key: String, vararg params: Any): String {
    return getTranslation(key, *params)
}