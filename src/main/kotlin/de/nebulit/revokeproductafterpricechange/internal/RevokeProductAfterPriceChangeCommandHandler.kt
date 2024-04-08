package de.nebulit.revokeproductafterpricechange.internal

import de.nebulit.common.*
import de.nebulit.common.persistence.InternalEvent
import org.springframework.stereotype.Component
import org.springframework.context.ApplicationEventPublisher
import de.nebulit.domain.CartAggregate
import java.util.UUID
import mu.KotlinLogging


@Component
class RevokeProductAfterPriceChangeCommandCommandHandler(
    private var aggregateService: AggregateService<CartAggregate>,
    private var applicationEventPublisher: ApplicationEventPublisher
) : BaseCommandHandler<CartAggregate>(aggregateService) {

    var logger = KotlinLogging.logger {}

    override fun handle(inputCommand: Command): List<InternalEvent> {
        assert(inputCommand is RevokeProductAfterPriceChangeCommand)
        val command = inputCommand as RevokeProductAfterPriceChangeCommand
        val aggregate = findAggregate(command.aggregateId)
        aggregate.revokeProduct(inputCommand.productId)
        aggregateService.persist(aggregate)
        aggregate.events.forEach {
             applicationEventPublisher.publishEvent(it.value as Any)
        }
        return aggregate.events
    }

    override fun supports(command: Command): Boolean {
        return command is RevokeProductAfterPriceChangeCommand
    }

}
