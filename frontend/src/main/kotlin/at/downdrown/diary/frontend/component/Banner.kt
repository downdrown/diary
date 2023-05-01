package at.downdrown.diary.frontend.component

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Tag
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.orderedlayout.VerticalLayout

@Tag("banner")
@CssImport("./css/component/banner.css")
class Banner(val heading: String, val content: String?) : VerticalLayout() {

    private val CSS_BASE = "banner"

    constructor(heading: String) : this(heading, null)

    init {
        setSizeUndefined()
        setWidthFull()
        addClassName(CSS_BASE)
        isPadding = false
        isSpacing = false

        add(heading())
        add(content())
    }

    private fun heading(): Component {
        val component = H1(heading)
        component.addClassName("heading")
        return component
    }

    private fun content(): Component {
        val component = Paragraph(content)
        component.addClassName("content")
        return component
    }
}
