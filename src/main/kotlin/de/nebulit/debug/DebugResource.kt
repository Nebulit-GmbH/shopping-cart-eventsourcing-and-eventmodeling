package de.nebulit.debug

import de.nebulit.common.Event
import de.nebulit.common.persistence.EventsEntityRepository
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

class DebugEvent(var event: Event) {

    var eventName:String

    init {
        eventName = event.javaClass.simpleName
    }
}

@RestController
class DebugEndpoint(
    var eventsEntityRepository: EventsEntityRepository
) {

    @CrossOrigin
    @GetMapping( value = [debug])
    fun debugData(@RequestParam("aggregateId") aggregateId: UUID):List<DebugEvent> {
        return eventsEntityRepository.findByAggregateId(aggregateId).mapNotNull { it.value }.map { DebugEvent(it) }
    }

    companion object {
        const val debug: String = "/debug"
    }

}
