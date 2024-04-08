package de.nebulit.domain

import de.nebulit.common.AggregateService
import de.nebulit.common.Query
import de.nebulit.common.QueryHandler
import de.nebulit.common.ReadModel
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.events.CartItem
import de.nebulit.events.CarttemAddedEvent
import java.util.UUID
import mu.KotlinLogging
import org.springframework.stereotype.Component

class CartItemsReadModelQuery(var aggregateId: UUID) : Query<UUID> {
    override fun toParam(): UUID {
        return aggregateId
    }
}

@Component
class CartItemsReadModelHandler(
    var aggregateService: AggregateService<CartAggregate>
) : QueryHandler<UUID, CartItems> {
    override fun handleQuery(query: Query<UUID>): CartItems {
        return CartItems().applyEvents(this.aggregateService.findEventsByAggregateId(query.toParam()))
    }

    override fun <T> canHandle(query: Query<T>): Boolean {
        return query is CartItemsReadModelQuery
    }
}

class CartItems : ReadModel<CartItems> {

    var logger = KotlinLogging.logger {}
    var cartItems = emptyMap<UUID, CartItem>()


    override fun applyEvents(events: List<InternalEvent>): CartItems {
        var cartItems = mutableMapOf<UUID, CartItem>()
        events.forEach {
            when (it.value) {
                is CarttemAddedEvent -> {
                    cartItems[(it.value as CarttemAddedEvent).cartItemId] =
                        CartItem(
                            (it.value as CarttemAddedEvent).cartItemId,
                            (it.value as CarttemAddedEvent).productId,
                            (it.value as CarttemAddedEvent).productName,
                            (it.value as CarttemAddedEvent).price,
                            (it.value as CarttemAddedEvent).quantity,
                            (it.value as CarttemAddedEvent).productimage
                        )
                }
            }
        }
        this.cartItems = cartItems
        return this
    }

}



