package de.nebulit.requestproductrevoke.internal

import de.nebulit.common.*
import de.nebulit.common.persistence.InternalEvent
import org.springframework.stereotype.Component
import org.springframework.context.ApplicationEventPublisher
import de.nebulit.domain.CartAggregate
import mu.KotlinLogging
import org.springframework.transaction.annotation.Transactional


@Component
class ExpireProductAfterPriceChangeCommandCommandHandler(
    private var aggregateService: AggregateService<CartAggregate>,
    private var applicationEventPublisher: ApplicationEventPublisher
) : BaseCommandHandler<CartAggregate>(aggregateService) {

    var logger = KotlinLogging.logger {}

    @Transactional
    override fun handle(inputCommand: Command): List<InternalEvent> {
        assert(inputCommand is RequestProductRevocationAfterPriceChangeCommand)
        val command = inputCommand as RequestProductRevocationAfterPriceChangeCommand
        val aggregate = findAggregate(command.aggregateId)
        aggregate.requestProductRevocationAfterPriceChange(command.productId)

        // TODO process logic
        aggregateService.persist(aggregate)
        aggregate.events.forEach {
             applicationEventPublisher.publishEvent(it.value as Any)
        }
        return aggregate.events
    }

    override fun supports(command: Command): Boolean {
        return command is RequestProductRevocationAfterPriceChangeCommand
    }

}
