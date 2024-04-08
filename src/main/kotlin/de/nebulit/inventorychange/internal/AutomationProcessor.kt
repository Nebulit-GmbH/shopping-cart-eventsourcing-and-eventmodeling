package de.nebulit.inventorychange.internal

import de.nebulit.common.Processor
import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import java.util.*


@Component
class AutomationProcessor(
    val eventsEntityRepository: EventsEntityRepository,
    val commandHandler: DelegatingCommandHandler
) : Processor {


    var logger = KotlinLogging.logger {}

    @ApplicationModuleListener
    fun onEvent(event: InventoryChangedEventExternal) {
        commandHandler.handle(ChangeInventoryCommand(UUID.randomUUID(),event.quantity))
    }

}

