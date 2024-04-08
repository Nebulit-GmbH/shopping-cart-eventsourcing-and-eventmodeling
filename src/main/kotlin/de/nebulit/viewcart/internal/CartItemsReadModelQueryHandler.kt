package de.nebulit.viewcart.internal

import de.nebulit.common.AggregateService
import de.nebulit.common.Query
import de.nebulit.common.QueryHandler
import de.nebulit.common.ReadModel
import de.nebulit.domain.CartAggregate
import de.nebulit.common.persistence.InternalEvent
import org.springframework.stereotype.Component
import de.nebulit.viewcart.CartItemsReadModel
import de.nebulit.viewcart.CartItemsReadModelQuery
import java.util.UUID
import mu.KotlinLogging

//TODO assumes can be rebuild from the eventstream
@Component
class CartItemsReadModelQueryHandler(
    private var aggregateService: AggregateService<CartAggregate>
) :
    QueryHandler<UUID, CartItemsReadModel> {

    override fun handleQuery(query: Query<UUID>): CartItemsReadModel {
        var aggregate = aggregateService.findByAggregateId(query.toParam())
            ?: throw IllegalStateException("Cart ${query.toParam()} not available")
        return CartItemsReadModel(aggregate.cartItemIds).applyEvents(aggregateService.findEventsByAggregateId(query.toParam()))
    }

    override fun <T> canHandle(query: Query<T>): Boolean {
        return query is CartItemsReadModelQuery
    }

}
