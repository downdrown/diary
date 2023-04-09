package at.downdrown.diary.frontend.event

import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.UI
import java.time.LocalDate

class DateSelectedEvent(val value: LocalDate) : ComponentEvent<UI>(UI.getCurrent(), true)
