package at.downdrown.diary.frontend.extensions

import com.vaadin.flow.data.binder.Binder

fun <T> Binder<T>.hasValidChanges(): Boolean {
    return hasChanges() && isValid
}