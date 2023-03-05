package at.downdrown.diary.frontend.view

import at.downdrown.diary.api.mood.MoodCheckInService
import at.downdrown.diary.api.security.userPrincipal
import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.frontend.dialog.MoodCheckInDialog
import at.downdrown.diary.frontend.extensions.i18n
import at.downdrown.diary.frontend.layout.MainLayout
import at.downdrown.diary.frontend.validation.Validators
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import java.time.LocalDate
import javax.annotation.security.PermitAll

@Route("/", layout = MainLayout::class)
@PermitAll
@CssImport("styles.css")
class TodayView(
    private val validators: Validators,
    private val userService: UserService,
    private val moodCheckInService: MoodCheckInService
) : VerticalLayout(), HasDynamicTitle, BeforeEnterObserver {

    private val principal = userPrincipal()

    init {

        val title = H1(i18n("app.name"))
        title.style
            .set("font-size", "var(--lumo-font-size-l)")
            .set("left", "var(--lumo-space-l)")
            .set("margin", "0")
            .set("position", "absolute")

        add(title)
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
