package de.nebulit.support

import de.nebulit.common.AggregateService
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.domain.CartAggregate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface CartAggregateRepository : CrudRepository<CartAggregate, Long> {
    fun findByAggregateId(aggregateId: UUID): CartAggregate?
}


@Component
class CartAggregateService(
    var repository: CartAggregateRepository,
    var eventsEntityRepository: EventsEntityRepository,
) : AggregateService<CartAggregate> {

    @Transactional
    override fun persist(aggregate: CartAggregate) {
        repository.save(aggregate)
        if (aggregate.events.isNotEmpty()) {
            eventsEntityRepository.saveAll(aggregate.events)
        }

    }

    override fun findByAggregateId(aggregateId: UUID): CartAggregate? {
        return repository.findByAggregateId(aggregateId)
    }

    override fun findEventsByAggregateId(aggregateId: UUID): List<InternalEvent> {
        return  eventsEntityRepository.findByAggregateIdAndIdGreaterThanOrderByIdAsc(
            aggregateId, 0)
    }

}
