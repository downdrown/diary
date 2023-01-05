package at.downdrown.diary.frontend.extensions

import at.downdrown.diary.frontend.view.View
import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinSession

fun UI.isMobile(): Boolean {
    val webBrowser = VaadinSession.getCurrent().browser
    return webBrowser.isAndroid || webBrowser.isIPhone || webBrowser.isWindowsPhone
}

fun navigate(view: View) {
    UI.getCurrent().navigate(view.location)
}