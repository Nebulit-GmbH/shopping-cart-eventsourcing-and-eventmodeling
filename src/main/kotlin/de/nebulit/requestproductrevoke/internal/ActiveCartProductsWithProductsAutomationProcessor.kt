package de.nebulit.requestproductrevoke.internal

import de.nebulit.activecartsessions.ActiveCartProductsWithProductsReadModel
import de.nebulit.activecartsessions.ActiveCartProductsWithProductsReadModelQuery
import de.nebulit.common.Processor
import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.DelegatingQueryHandler
import de.nebulit.common.persistence.EventsEntityRepository
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component
import mu.KotlinLogging

import de.nebulit.events.PriceChangedEvent
import java.util.*

@Component
class ActiveCartProductsWithProductsAutomationProcessor(
 val eventsEntityRepository: EventsEntityRepository,
 val delegatingQueryHandler:DelegatingQueryHandler,
 val commandHandler: DelegatingCommandHandler): Processor {

	@ApplicationModuleListener
	fun on(event: PriceChangedEvent) {
	     logger.info("Processing PriceChangedEvent")
        var readModel = delegatingQueryHandler.handleQuery<UUID, ActiveCartProductsWithProductsReadModel> (ActiveCartProductsWithProductsReadModelQuery(event.aggregateId))
        readModel.cartIds.forEach { cartId: UUID ->
            commandHandler.handle(RequestProductRevocationAfterPriceChangeCommand(cartId, event.aggregateId))
        }
    }

   var logger = KotlinLogging.logger {}


}

