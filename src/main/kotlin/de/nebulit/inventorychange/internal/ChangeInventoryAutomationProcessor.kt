package de.nebulit.inventorychange.internal

import de.nebulit.common.Processor
import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component
import mu.KotlinLogging


@Component
class ChangeInventoryAutomationProcessor(
    val eventsEntityRepository: EventsEntityRepository,
    val commandHandler: DelegatingCommandHandler
) : Processor {


    var logger = KotlinLogging.logger {}

    @ApplicationModuleListener
    fun onEvent(event: InventoryChangedEventExternal) {
        commandHandler.handle(ChangeInventoryCommand(event.productId,event.quantity))
    }

}

