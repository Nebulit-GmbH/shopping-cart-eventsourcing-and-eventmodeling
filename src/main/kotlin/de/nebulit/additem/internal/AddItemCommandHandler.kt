package de.nebulit.additem.internal

import de.nebulit.common.*
import de.nebulit.common.persistence.InternalEvent
import org.springframework.stereotype.Component
import org.springframework.context.ApplicationEventPublisher
import de.nebulit.domain.CartAggregate
import de.nebulit.events.CartItem
import mu.KotlinLogging
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class AddItemCommandCommandHandler(
    private var aggregateService: AggregateService<CartAggregate>,
    private var applicationEventPublisher: ApplicationEventPublisher
) : BaseCommandHandler<CartAggregate>(aggregateService) {

    var logger = KotlinLogging.logger {}

    @Transactional
    override fun handle(inputCommand: Command): List<InternalEvent> {
        assert(inputCommand is AddItemCommand)
        val command = inputCommand as AddItemCommand
        var aggregate = aggregateService.findByAggregateId(command.aggregateId)
        if (aggregate == null) {
            aggregate = CartAggregate.newSession(command.aggregateId)
        }
        aggregate.addItem(CartItem(UUID.randomUUID(), command.productId, command.productName, command.price, command.quantity, command.productimage))
        aggregateService.persist(aggregate)
        aggregate.events.forEach {
             applicationEventPublisher.publishEvent(it.value as Any)
        }
        return aggregate.events
    }

    override fun supports(command: Command): Boolean {
        return command is AddItemCommand
    }

}
