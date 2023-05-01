package at.downdrown.diary.frontend.view

import at.downdrown.diary.frontend.layout.GridLayout
import at.downdrown.diary.frontend.layout.MainLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.Route
import jakarta.annotation.security.PermitAll

@Route("/dev/icons", layout = MainLayout::class)
@PermitAll
class DevIconView: GridLayout() {

    private val componentMapping = HashMap<VaadinIcon, Component>()

    init {
        addExpanded(searchField())
        for (icon in VaadinIcon.values()) {
            val component = toComponent(icon)
            componentMapping.put(icon, component)
            add(component)
        }
    }

    private fun searchField(): Component {
        val searchField = TextField(null, "Search here")
        searchField.prefixComponent = Icon(VaadinIcon.SEARCH)
        searchField.suffixComponent = Button(Icon(VaadinIcon.CLOSE_BIG)) {
            searchField.clear()
            resetSearch()
        }
        searchField.valueChangeTimeout = 500
        searchField.valueChangeMode = ValueChangeMode.EAGER
        searchField.addValueChangeListener { event -> search(event.value) }
        return searchField
    }

    private fun search(searchPhrase: String?) {
        resetSearch()
        if (!searchPhrase.isNullOrBlank()){
            componentMapping.entries.forEach{ entry -> if (!entry.key.name.lowercase().contains(searchPhrase.lowercase())) { entry.value.isVisible = false } }
        }
    }

    private fun resetSearch() {
        componentMapping.entries.forEach { entry -> entry.value.isVisible = true }
    }

    private fun toComponent(icon: VaadinIcon): Component {
        val layout = VerticalLayout(
            icon.create(),
            Label(icon.name)
        )
        layout.defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        layout.alignItems = FlexComponent.Alignment.CENTER
        return layout
    }
}
