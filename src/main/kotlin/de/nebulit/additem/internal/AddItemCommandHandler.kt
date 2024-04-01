package de.nebulit.additem.internal

import de.nebulit.common.*
import de.nebulit.common.persistence.InternalEvent
import org.springframework.stereotype.Component
import org.springframework.context.ApplicationEventPublisher
import de.nebulit.domain.CartAggregate
import de.nebulit.domain.CartItem
import java.util.UUID
import mu.KotlinLogging

@Component
class AddItemCommandCommandHandler(
    private var aggregateService: AggregateService<CartAggregate>,
    private var applicationEventPublisher: ApplicationEventPublisher
) : BaseCommandHandler<CartAggregate>(aggregateService) {

    var logger = KotlinLogging.logger {}

    override fun handle(inputCommand: Command): List<InternalEvent> {
        assert(inputCommand is AddItemCommand)
        val command = inputCommand as AddItemCommand
        var aggregate = aggregateService.findByAggregateId(command.aggregateId)
        if (aggregate == null) {
            aggregate = CartAggregate.newSession(command.aggregateId)
        }
        aggregate.addItem(CartItem(command.productName, command.price, command.quantity, command.productimage, command.productId))
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
