package at.downdrown.diary.frontend.event

import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentUtil
import com.vaadin.flow.component.UI
import mu.KotlinLogging
import java.time.LocalDate
import java.time.YearMonth

private val log = KotlinLogging.logger {}

open class DiaryEvent : ComponentEvent<UI> (UI.getCurrent(), true)
class DateSelectedEvent(val value: LocalDate) : DiaryEvent()
class YearMonthSelectedEvent(val value: YearMonth) : DiaryEvent()

fun <EventType : DiaryEvent> publishEvent(event: EventType) {
    log.trace { "Publishing ${event.javaClass.simpleName} with UI=${event.source}" }
    ComponentUtil.fireEvent(
        event.source,
        event
    )
}

fun <EventType : DiaryEvent> onEvent(eventType: Class<EventType>, callback: (event: EventType) -> Unit) {
    val currentUI = UI.getCurrent()
    log.trace { "Registering callback for eventType ${eventType.simpleName} with UI=${currentUI}" }
    ComponentUtil.addListener(
        currentUI,
        eventType,
    ) { event ->
        callback(event)
    }
}
