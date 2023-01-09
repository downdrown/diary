package at.downdrown.diary.frontend.component

import com.vaadin.flow.component.Composite
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.dom.ThemeList
import com.vaadin.flow.theme.lumo.Lumo

class ThemeToggle : Composite<Button>() {

    private val lightIcon = VaadinIcon.SUN_O
    private val darkIcon = VaadinIcon.MOON_O

    init {
        content.icon = getToggleIconForCurrentTheme()
        content.addThemeVariants(ButtonVariant.LUMO_ICON)
        content.element.setAttribute("aria-label", getTranslation("themeToggle.aria-label"))
        content.addClickListener { toggleTheme() }
        content.addClickListener { toggleIcon() }
    }

    private fun toggleTheme() {
        if (isDarkMode()) {
            themeList().remove(Lumo.DARK)
        } else {
            themeList().add(Lumo.DARK)
        }
    }

    private fun toggleIcon() {
        content.icon = getToggleIconForCurrentTheme()
    }

    private fun getToggleIconForCurrentTheme(): Icon {
        return if (isDarkMode()) Icon(lightIcon) else Icon(darkIcon)
    }
}

private fun themeList(): ThemeList {
    return UI.getCurrent().element.themeList
}

private fun isDarkMode(): Boolean {
    return themeList().contains(Lumo.DARK)
}
