package de.nebulit.clearcart.internal

import de.nebulit.common.*
import de.nebulit.common.persistence.InternalEvent
import org.springframework.stereotype.Component
import org.springframework.context.ApplicationEventPublisher
import de.nebulit.domain.CartAggregate
import java.util.UUID
import mu.KotlinLogging
import org.springframework.transaction.annotation.Transactional


@Component
class ClearCartCommandCommandHandler(
    private var aggregateService: AggregateService<CartAggregate>,
    private var applicationEventPublisher: ApplicationEventPublisher
) : BaseCommandHandler<CartAggregate>(aggregateService) {

    var logger = KotlinLogging.logger {}

    @Transactional
    override fun handle(inputCommand: Command): List<InternalEvent> {
        assert(inputCommand is ClearCartCommand)
        val command = inputCommand as ClearCartCommand
        val aggregate = findAggregate(command.aggregateId)
        // TODO process logic
        aggregate.clear()
        aggregateService.persist(aggregate)
        aggregate.events.forEach {
             applicationEventPublisher.publishEvent(it.value as Any)
        }
        return aggregate.events
    }

    override fun supports(command: Command): Boolean {
        return command is ClearCartCommand
    }

}
