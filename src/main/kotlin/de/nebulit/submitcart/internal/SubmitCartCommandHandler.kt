package de.nebulit.submitcart.internal

import de.nebulit.common.*
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.common.persistence.InternalEvent
import org.springframework.stereotype.Component
import org.springframework.context.ApplicationEventPublisher
import de.nebulit.domain.CartAggregate
import mu.KotlinLogging
import org.springframework.transaction.annotation.Transactional


@Component
class SubmitCartCommandCommandHandler(
    private var aggregateService: AggregateService<CartAggregate>,
    private var eventsEntityRepository: EventsEntityRepository,
    private var applicationEventPublisher: ApplicationEventPublisher
) : BaseCommandHandler<CartAggregate>(aggregateService) {

    var logger = KotlinLogging.logger {}

    @Transactional
    override fun handle(inputCommand: Command): List<InternalEvent> {
        assert(inputCommand is SubmitCartCommand)
        val command = inputCommand as SubmitCartCommand
        val aggregate = findAggregate(command.aggregateId)
        aggregate.submit()
        // TODO process logic
        aggregateService.persist(aggregate)
        aggregate.events.forEach {
             applicationEventPublisher.publishEvent(it.value as Any)
        }
        return aggregate.events
    }

    override fun supports(command: Command): Boolean {
        return command is SubmitCartCommand
    }

}
