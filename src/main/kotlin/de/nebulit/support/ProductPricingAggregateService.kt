package de.nebulit.support

import de.nebulit.common.AggregateService
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.domain.ProductPricingAggregate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface ProductPricingAggregateRepository : CrudRepository<ProductPricingAggregate, Long> {
    fun findByAggregateId(aggregateId: UUID): ProductPricingAggregate?
}


@Component
class ProductPricingAggregateService(
    var repository: ProductPricingAggregateRepository,
    var eventsEntityRepository: EventsEntityRepository,
) : AggregateService<ProductPricingAggregate> {

    @Transactional
    override fun persist(aggregate: ProductPricingAggregate) {
        repository.save(aggregate)
        if (aggregate.events.isNotEmpty()) {
            eventsEntityRepository.saveAll(aggregate.events)
        }

    }

    override fun findByAggregateId(aggregateId: UUID): ProductPricingAggregate? {
        var events = findEventsByAggregateId(aggregateId)
        var aggregate = repository.findByAggregateId(aggregateId)
        aggregate?.applyEvents(events)
        return aggregate
    }

    override fun findEventsByAggregateId(aggregateId: UUID): List<InternalEvent> {
        return eventsEntityRepository.findByAggregateIdAndIdGreaterThanOrderByIdAsc(
            aggregateId, 0
        )
    }

}
