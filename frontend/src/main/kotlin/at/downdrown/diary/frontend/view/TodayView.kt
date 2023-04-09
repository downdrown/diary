package at.downdrown.diary.frontend.view

import at.downdrown.diary.api.mood.MoodCheckInService
import at.downdrown.diary.api.security.userPrincipal
import at.downdrown.diary.frontend.dialog.MoodCheckInDialog
import at.downdrown.diary.frontend.event.DateSelectedEvent
import at.downdrown.diary.frontend.extensions.i18n
import at.downdrown.diary.frontend.layout.MainLayout
import com.vaadin.flow.component.ComponentUtil
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import mu.KotlinLogging
import java.time.LocalDate
import javax.annotation.security.PermitAll

private val log = KotlinLogging.logger {}

@Route("/", layout = MainLayout::class)
@PermitAll
@CssImport("styles.css")
class TodayView(
    private val moodCheckInService: MoodCheckInService
) : VerticalLayout(), HasDynamicTitle, BeforeEnterObserver {

    init {

        val title = H1(i18n("app.name"))
        title.add(i18n("app.name"))
        title.style
            .set("font-size", "var(--lumo-font-size-l)")
            .set("left", "var(--lumo-space-l)")
            .set("margin", "0")
            .set("position", "absolute")

        add(title)

        ComponentUtil.addListener(
            UI.getCurrent(),
            DateSelectedEvent::class.java
        ) { event -> log.trace { "User selected new Date: ${event.value}" } }
    }

    override fun getPageTitle(): String {
        return i18n("main.pagetitle")
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        showMoodCheckInAtLeastOnceADay()
    }

    private fun showMoodCheckInAtLeastOnceADay() {
        if (!moodCheckInService.hasAlreadyCheckedIn(userPrincipal().username, LocalDate.now())) {
            MoodCheckInDialog(false, moodCheckInService).open()
        }
    }
}
