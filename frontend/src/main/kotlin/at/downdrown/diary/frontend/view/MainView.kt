package at.downdrown.diary.frontend.view

import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.frontend.dialog.ChangePasswordDialog
import at.downdrown.diary.frontend.dialog.ProfileDialog
import at.downdrown.diary.frontend.extensions.i18n
import at.downdrown.diary.frontend.extensions.userPrincipal
import at.downdrown.diary.frontend.validation.Validators
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.contextmenu.SubMenu
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.menubar.MenuBarVariant
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.server.VaadinServletRequest
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import javax.annotation.security.PermitAll

@Route("/")
@PermitAll
@CssImport("styles.css")
class MainView(
    private val validators: Validators,
    private val userService: UserService
) : AppLayout(), HasDynamicTitle {

    private val principal = userPrincipal()

    init {

        val title = H1(i18n("app.name"))
        title.style
            .set("font-size", "var(--lumo-font-size-l)")
            .set("left", "var(--lumo-space-l)")
            .set("margin", "0")
            .set("position", "absolute")

        val menuBar = MenuBar()
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE)

        val avatar = Avatar(principal.fullname)

        val menuItem: MenuItem = menuBar.addItem(avatar)
        val subMenu: SubMenu = menuItem.subMenu
        subMenu.addItem(i18n("main.usermenu.profile")) { ProfileDialog(validators, userService).open() }
        subMenu.addItem(i18n("main.usermenu.changepassword")) { ChangePasswordDialog(validators, userService).open() }
        subMenu.addItem(i18n("main.usermenu.settings"))
        subMenu.addItem(i18n("main.usermenu.help"))
        subMenu.addItem(i18n("main.usermenu.logout")) { logout() }

        val tabs = getTabs()

        addToNavbar(true, title, tabs, menuBar)
    }

    private fun getTabs(): Tabs {
        val tabs = Tabs()
        tabs.style
            .set("margin", "auto")
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

    override fun getPageTitle(): String {
        return i18n("main.pagetitle")
    }

    private fun logout() {
        UI.getCurrent().page.setLocation(View.Login.location)
        val httpServletRequest = VaadinServletRequest.getCurrent().httpServletRequest
        httpServletRequest.logout()
        SecurityContextLogoutHandler().logout(httpServletRequest, null, null)
    }
}
