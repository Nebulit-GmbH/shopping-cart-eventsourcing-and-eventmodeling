package de.nebulit.inventorychange.internal

import de.nebulit.common.*
import de.nebulit.common.persistence.InternalEvent
import org.springframework.stereotype.Component
import org.springframework.context.ApplicationEventPublisher
import de.nebulit.domain.ProductInventoryAggregate
import java.util.UUID
import mu.KotlinLogging
import org.springframework.transaction.annotation.Transactional


@Component
class ChangeInventoryCommandCommandHandler(
    private var aggregateService: AggregateService<ProductInventoryAggregate>,
    private var applicationEventPublisher: ApplicationEventPublisher
) : BaseCommandHandler<ProductInventoryAggregate>(aggregateService) {

    var logger = KotlinLogging.logger {}

    @Transactional
    override fun handle(inputCommand: Command): List<InternalEvent> {
        assert(inputCommand is ChangeInventoryCommand)
        val command = inputCommand as ChangeInventoryCommand
        var aggregate = aggregateService.findByAggregateId(inputCommand.aggregateId)

        if (aggregate == null) {
            aggregate = ProductInventoryAggregate(command.aggregateId)
        }
        aggregate.inventoryChanged(command.quantity)
        aggregateService.persist(aggregate)
        aggregate.events.forEach {
             applicationEventPublisher.publishEvent(it.value as Any)
        }
        return aggregate.events
    }

    override fun supports(command: Command): Boolean {
        return command is ChangeInventoryCommand
    }

}
