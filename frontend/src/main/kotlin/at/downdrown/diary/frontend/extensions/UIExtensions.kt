package at.downdrown.diary.frontend.extensions

import at.downdrown.diary.frontend.view.View
import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinSession

fun isMobile(): Boolean {
    val webBrowser = VaadinSession.getCurrent().browser
    return webBrowser.isAndroid || webBrowser.isIPhone || webBrowser.isWindowsPhone
}

fun i18n(key: String, vararg params: Any): String {
    return UI.getCurrent().getTranslation(key, *params)
}

fun navigate(view: View) {
    UI.getCurrent().navigate(view.navigationTarget)
}
