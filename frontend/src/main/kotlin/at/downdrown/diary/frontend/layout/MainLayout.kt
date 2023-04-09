package at.downdrown.diary.frontend.layout

import at.downdrown.diary.api.security.userPrincipal
import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.frontend.dialog.ChangePasswordDialog
import at.downdrown.diary.frontend.dialog.ProfileDialog
import at.downdrown.diary.frontend.extensions.i18n
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
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.menubar.MenuBarVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.tabs.TabsVariant
import com.vaadin.flow.router.HighlightAction
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.theme.lumo.Lumo
import elemental.json.impl.JreJsonString
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
        addToNavbar(navigation())
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

    private fun navigation(): Component {
        val navigationLayout = HorizontalLayout(
            drawerToggle(),
            tabs(),
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

    private fun tabs(): Component {
        val tabs = Tabs()
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL)
        tabs.isAutoselect = false
        tabs.style["margin"] = "auto"
        tabs.add(navigationItems(tabs))
        return tabs
    }

    private fun navigationItems(tabs: Tabs): List<Component> {
        return listOf(
            navigationTab(tabs, View.Today),
            navigationTab(tabs, View.Registration)
        )
    }

    private fun navigationTab(tabs: Tabs, view: View): Tab {
        val link = RouterLink(view.navigationTarget)
        link.highlightCondition = HighlightConditions.sameLocation()
        link.add(i18n(view.i18nKey))
        link.tabIndex = -1
        val tab = Tab(link)
        link.highlightAction =
            HighlightAction { _: RouterLink?, highlight: Boolean ->
                if (highlight) {
                    tabs.selectedTab = tab
                }
            }
        return tab
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
}
