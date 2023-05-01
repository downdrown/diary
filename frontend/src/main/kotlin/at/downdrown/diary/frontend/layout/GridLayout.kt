package at.downdrown.diary.frontend.layout

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Tag
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div

@Tag("grid")
@CssImport("./css/layout/grid.css")
open class GridLayout: Div() {

    private val cssBase = "grid"
    private val cssGridSpan = "span"

     init {
         this.addClassName(cssBase)
     }

    fun addExpanded(component: Component) {
        component.addClassName(cssGridSpan)
        add(component)
    }
}
