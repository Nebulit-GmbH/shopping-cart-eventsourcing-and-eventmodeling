package de.nebulit.revokeproductafterpricechange.internal

import de.nebulit.common.Processor
import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.DelegatingQueryHandler
import de.nebulit.common.persistence.EventsEntityRepository
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component
import mu.KotlinLogging

import de.nebulit.events.ProductRevocationRequestedEvent

@Component
class AutomationProcessor(
    val eventsEntityRepository: EventsEntityRepository,
    val delegatingQueryHandler: DelegatingQueryHandler,
    val commandHandler: DelegatingCommandHandler
) : Processor {

    @ApplicationModuleListener
    fun on(event: ProductRevocationRequestedEvent) {
        logger.info("Processing ProductRevocationRequestedEvent")
        commandHandler.handle(RevokeProductAfterPriceChangeCommand(event.aggregateId, event.productId))
    }

    var logger = KotlinLogging.logger {}


}

