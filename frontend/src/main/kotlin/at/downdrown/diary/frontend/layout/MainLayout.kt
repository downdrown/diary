package at.downdrown.diary.frontend.layout

import at.downdrown.diary.api.security.userPrincipal
import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.frontend.dialog.ChangePasswordDialog
import at.downdrown.diary.frontend.dialog.ProfileDialog
import at.downdrown.diary.frontend.event.DateSelectedEvent
import at.downdrown.diary.frontend.event.YearMonthSelectedEvent
import at.downdrown.diary.frontend.event.publishEvent
import at.downdrown.diary.frontend.extensions.i18n
import at.downdrown.diary.frontend.extensions.isMobile
import at.downdrown.diary.frontend.service.AuthenticationService
import at.downdrown.diary.frontend.validation.Validators
import at.downdrown.diary.frontend.view.View
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.contextmenu.SubMenu
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.dependency.JsModule
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.menubar.MenuBarVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.tabs.TabsVariant
import com.vaadin.flow.router.HighlightAction
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.theme.lumo.Lumo
import elemental.json.impl.JreJsonString
import org.vaadin.addons.minicalendar.MiniCalendar
import org.vaadin.addons.minicalendar.MiniCalendarVariant
import java.time.LocalDate
import java.util.*

@JsModule("./js/os-theme-module.js")
@CssImport("./css/mainlayout.css")
@PreserveOnRefresh
class MainLayout(
    private val validators: Validators,
    private val userService: UserService,
    private val authenticationService: AuthenticationService
) : AppLayout() {

    init {
        initEnvironment()
        addToNavbar(header())
        addToDrawer(drawer())
    }

    private fun initEnvironment() {
        UI.getCurrent().page
            .executeJs("return getUserPreferredColorScheme()")
            .then(JreJsonString::class.java) { userPreferredTheme: JreJsonString ->
                toggleThemeTo(userPreferredTheme.asString())
            }
    }

    private fun toggleThemeTo(selectedTheme: String) {
        val themeList = UI.getCurrent().element.themeList
        val deselectedTheme = if (selectedTheme == Lumo.DARK) Lumo.LIGHT else Lumo.DARK
        themeList[selectedTheme] = true
        themeList[deselectedTheme] = false
    }

    private fun header(): Component {
        val navigationLayout = HorizontalLayout(
            drawerToggle(),
            if (isMobile()) appLabel() else navigation(),
            userMenu()
        )
        navigationLayout.setWidthFull()
        navigationLayout.setMinHeight(60f, Unit.PIXELS)
        navigationLayout.defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
        navigationLayout.alignItems = FlexComponent.Alignment.CENTER
        navigationLayout.isSpacing = true
        navigationLayout.isPadding = true
        return navigationLayout
    }

    private fun drawerToggle(): Component {
        val toggle = DrawerToggle()
        toggle.addThemeVariants(ButtonVariant.LUMO_SMALL)
        toggle.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST)
        toggle.icon = VaadinIcon.MENU.create()
        return toggle
    }

    private fun navigation(): Component {
        val tabs = Tabs()
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL)
        tabs.isAutoselect = false
        tabs.style["margin"] = "auto"
        tabs.add(*navigationTabs(tabs).toTypedArray())
        return tabs
    }

    private fun navigationTabs(tabs: Tabs): List<Tab> {
        if (isMobile()) {
            return emptyList()
        }
        return listOf(
            navigationTab(tabs, View.Today),
            navigationTab(tabs, View.Registration)
        )
    }

    private fun navigationTab(tabs: Tabs, view: View): Tab {
        val link = routerLink(view)
        val tab = Tab(link)
        link.highlightAction =
            HighlightAction { _: RouterLink?, highlight: Boolean ->
                if (highlight) {
                    tabs.selectedTab = tab
                }
            }
        return tab
    }

    private fun routerLink(view: View): RouterLink {
        val link = RouterLink(view.navigationTarget)
        link.highlightCondition = HighlightConditions.sameLocation()
        link.add(i18n(view.i18nKey))
        link.tabIndex = -1
        return link
    }

    private fun appLabel(): Component {
        val title = H1(i18n("app.name"))
        title.setWidthFull()
        title.style
            .set("font-size", "var(--lumo-font-size-l)")
            .set("text-align", "center")
            .set("margin", "0")
        return title
    }

    private fun userMenu(): Component {
        val avatar = Avatar(userPrincipal().fullname)

        val menuBar = MenuBar()
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE)

        val menuItem: MenuItem = menuBar.addItem(avatar)
        val subMenu: SubMenu = menuItem.subMenu
        subMenu.addItem(i18n("main.usermenu.profile")) { ProfileDialog(validators, userService).open() }
        subMenu.addItem(i18n("main.usermenu.changepassword")) { ChangePasswordDialog(validators, userService).open() }
        subMenu.addItem(i18n("main.usermenu.settings"))
        subMenu.addItem(i18n("main.usermenu.help"))
        subMenu.addItem(i18n("main.usermenu.logout")) { authenticationService.logout() }

        return menuBar
    }

    private fun drawer(): Component {
        val layout = VerticalLayout(
            *drawerItems().toTypedArray()
        )
        layout.style.set("margin-top", "10px")
        layout.setWidthFull()
        layout.defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        layout.justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        return layout
    }

    private fun drawerItems(): List<Component> {
        val items = mutableListOf(miniCalendar())
        if (isMobile()) {
            items.addAll(navigationLinks())
        }
        return items
    }

    private fun navigationLinks(): List<Component> {
        return listOf(
            navigationLink(View.Today)
        )
    }

    private fun navigationLink(view: View): Component {
        val routerLink = routerLink(view)
        routerLink.highlightAction = HighlightAction { _: RouterLink?, highlight: Boolean ->
            if (highlight) {
                // TODO highlight current route
            }
        }
        return routerLink
    }

    private fun miniCalendar(): Component {
        val miniCalendar = MiniCalendar()
        miniCalendar.value = LocalDate.now()
        miniCalendar.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_CURRENT_DAY)
        miniCalendar.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_WEEKEND)
        miniCalendar.addValueChangeListener { event ->
            publishEvent(
                DateSelectedEvent(event.value)
            )
        }
        miniCalendar.addYearMonthChangeListener { event ->
            publishEvent(
                YearMonthSelectedEvent(event.value)
            )
        }
        return miniCalendar
    }
}
