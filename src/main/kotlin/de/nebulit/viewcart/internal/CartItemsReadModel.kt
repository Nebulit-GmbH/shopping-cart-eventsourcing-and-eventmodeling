package de.nebulit.viewcart.internal

import de.nebulit.common.AggregateService
import de.nebulit.common.Query
import de.nebulit.common.QueryHandler
import de.nebulit.common.ReadModel
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.domain.CartAggregate
import de.nebulit.domain.TotalPrice
import de.nebulit.events.CartClearedEvent
import de.nebulit.events.CartItemRemovedEvent
import de.nebulit.events.CarttemAddedEvent
import java.util.UUID
import mu.KotlinLogging
import org.springframework.stereotype.Component

class CartItem(
    var cartItemId: UUID,
    var productName: String,
    var price: Double?,
    var quantity: Int,
    var productimage: String,
)

class CartItemsReadModelQuery(var aggregateId: UUID):Query<UUID> {
    override fun toParam(): UUID {
        return aggregateId
    }
}

@Component
class CartItemsReadModelQueryHandler(var aggregateService: AggregateService<CartAggregate>) :
    QueryHandler<UUID, CartItemsReadModel> {
    override fun handleQuery(query: Query<UUID>): CartItemsReadModel {
        return CartItemsReadModel().applyEvents(aggregateService.findEventsByAggregateId(query.toParam()))
    }

    override fun <T> canHandle(query: Query<T>): Boolean {
        return query is CartItemsReadModelQuery
    }

}


class CartItemsReadModel : ReadModel<CartItemsReadModel> {

    var logger = KotlinLogging.logger {}
    var cartItems = emptyMap<UUID, CartItem>()
    var totalPrice: Double = 0.0


    override fun applyEvents(events: List<InternalEvent>): CartItemsReadModel {
        var cartItems = mutableMapOf<UUID, CartItem>()
        events.forEach {
            when (it.value) {
                is CarttemAddedEvent -> {
                    cartItems[(it.value as CarttemAddedEvent).cartItemId] =
                        CartItem(
                            (it.value as CarttemAddedEvent).cartItemId,
                            (it.value as CarttemAddedEvent).productName,
                            (it.value as CarttemAddedEvent).price,
                            (it.value as CarttemAddedEvent).quantity,
                            (it.value as CarttemAddedEvent).productimage
                        )
                }
                is CartItemRemovedEvent -> {
                    cartItems.remove((it.value as CartItemRemovedEvent).cartItemId)
                }
                is CartClearedEvent -> cartItems.clear()
            }
        }
        this.totalPrice += TotalPrice().applyEvents(events).totalPrice
        this.cartItems = cartItems
        return this
    }

}



