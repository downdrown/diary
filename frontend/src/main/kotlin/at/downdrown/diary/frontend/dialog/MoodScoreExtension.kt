package at.downdrown.diary.frontend.dialog

import at.downdrown.diary.api.mood.MoodScore
import at.downdrown.diary.frontend.extensions.i18n
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.html.Span

fun MoodScore.icon() : Component {
    val emojiSpan = Span(i18n("moodscore.${this.name.lowercase()}.emoji"))
    emojiSpan.style.set("font-size", "var(--lumo-font-size-xxxl)")
    return emojiSpan
}

fun MoodScore.label() : Label {
    return Label(i18n("moodscore.${this.name.lowercase()}.label"))
}
