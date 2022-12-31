package at.downdrown.diary.frontend.view

import at.downdrown.diary.frontend.component.ThemeToggle
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLink

@Route("/")
class MainView : AppLayout() {

    init {

        val title = H1(getTranslation("app.name"))
        title.style.set("font-size", "var(--lumo-font-size-l)")
            .set("left", "var(--lumo-space-l)").set("margin", "0")["position"] = "absolute"

        val tabs = getTabs()

        addToNavbar(true,  title, tabs, ThemeToggle())
    }

    private fun getTabs(): Tabs {
        val tabs = Tabs()
        tabs.style["margin"] = "auto"
        tabs.add(
            createTab(getTranslation("nav.dashboard")),
            createTab(getTranslation("nav.today")),
            createTab(getTranslation("nav.entries"))
        )
        return tabs
    }

    private fun createTab(viewName: String): Tab {
        val link = RouterLink()
        link.add(viewName)
        link.tabIndex = -1
        return Tab(link)
    }
}