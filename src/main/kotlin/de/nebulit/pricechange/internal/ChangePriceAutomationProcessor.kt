package de.nebulit.pricechange.internal

import de.nebulit.common.Processor
import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component
import mu.KotlinLogging


@Component
class ChangePriceAutomationProcessor(
    val eventsEntityRepository: EventsEntityRepository,
    val commandHandler: DelegatingCommandHandler
) : Processor {


    var logger = KotlinLogging.logger {}

    @ApplicationModuleListener
    fun process(priceChanged: PriceChangedEventExternal) {
        commandHandler.handle(
            ChangePriceCommand(
                priceChanged.aggregateId,
                priceChanged.oldPrice,
                priceChanged.newPrice
            )
        )
    }

}

