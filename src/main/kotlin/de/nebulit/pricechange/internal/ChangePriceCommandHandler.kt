package de.nebulit.pricechange.internal

import de.nebulit.common.*
import de.nebulit.common.persistence.InternalEvent
import org.springframework.stereotype.Component
import org.springframework.context.ApplicationEventPublisher
import de.nebulit.domain.ProductPricingAggregate

import mu.KotlinLogging
import org.springframework.transaction.annotation.Transactional


@Component
class ChangePriceCommandCommandHandler(
    private var aggregateService: AggregateService<ProductPricingAggregate>,
    private var applicationEventPublisher: ApplicationEventPublisher
) : BaseCommandHandler<ProductPricingAggregate>(aggregateService) {

    var logger = KotlinLogging.logger {}

    @Transactional
    override fun handle(inputCommand: Command): List<InternalEvent> {
        assert(inputCommand is ChangePriceCommand)
        val command = inputCommand as ChangePriceCommand
        var aggregate = aggregateService.findByAggregateId(command.aggregateId)
        if (aggregate == null) {
            aggregate = ProductPricingAggregate(command.aggregateId)
        }
        aggregate.applyEvents(aggregateService.findEventsByAggregateId(command.aggregateId))
        aggregate.priceChanged(inputCommand.oldPrice, inputCommand.newPrice)
        aggregateService.persist(aggregate)
        aggregate.events.forEach {
             applicationEventPublisher.publishEvent(it.value as Any)
        }
        return aggregate.events
    }

    override fun supports(command: Command): Boolean {
        return command is ChangePriceCommand
    }

}
