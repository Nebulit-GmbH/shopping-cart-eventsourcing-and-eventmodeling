package de.nebulit.support

import de.nebulit.common.AggregateService
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.domain.ProductInventoryAggregate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface ProductInventoryAggregateRepository : CrudRepository<ProductInventoryAggregate, Long> {
    fun findByAggregateId(aggregateId: UUID): ProductInventoryAggregate?
}


@Component
class ProductInventoryAggregateService(
    var repository: ProductInventoryAggregateRepository,
    var eventsEntityRepository: EventsEntityRepository,
) : AggregateService<ProductInventoryAggregate> {

    @Transactional
    override fun persist(aggregate: ProductInventoryAggregate) {
        repository.save(aggregate)
        if (aggregate.events.isNotEmpty()) {
            eventsEntityRepository.saveAll(aggregate.events)
        }

    }

    override fun findByAggregateId(aggregateId: UUID): ProductInventoryAggregate? {
        return repository.findByAggregateId(aggregateId)
    }

    override fun findEventsByAggregateId(aggregateId: UUID): List<InternalEvent> {
        return  eventsEntityRepository.findByAggregateIdAndIdGreaterThanOrderByIdAsc(
            aggregateId, 0)
    }

}
