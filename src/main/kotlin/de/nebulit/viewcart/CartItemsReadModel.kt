package de.nebulit.viewcart

import com.fasterxml.jackson.annotation.JsonIgnore
import de.nebulit.common.AggregateService
import de.nebulit.common.Query
import de.nebulit.common.QueryHandler
import de.nebulit.common.ReadModel
import de.nebulit.domain.CartAggregate
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.domain.TotalPrice
import de.nebulit.events.CartClearedEvent
import de.nebulit.events.CartItemRemovedEvent
import de.nebulit.events.CarttemAddedEvent
import de.nebulit.events.ProductRevokedEvent
import org.springframework.stereotype.Component
import java.util.UUID
import mu.KotlinLogging

//TODO adjust the query to the necessary parameters
class CartItemsReadModelQuery(var aggregateId: UUID) : Query<UUID> {
    override fun toParam(): UUID {
        return aggregateId
    }
}

class CartItem(
    var cartItemId: UUID,
    var productId: UUID,
    var productName: String,
    var price: Double?,
    var quantity: Int,
    var productimage: String,
)

class CartItemsReadModel(private var cartItemsInCart: Set<UUID>) : ReadModel<CartItemsReadModel> {

    @JsonIgnore
    var logger = KotlinLogging.logger {}
    var cartItems = emptyMap<UUID, CartItem>()
    var totalPrice: Double = 0.0


    override fun applyEvents(events: List<InternalEvent>): CartItemsReadModel {
        var cartItems = mutableMapOf<UUID, CartItem>()
        events.forEach {
            when (it.value) {
                is CarttemAddedEvent -> {
                    if (cartItemsInCart.contains((it.value as CarttemAddedEvent).cartItemId)) {
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
        }
        this.totalPrice += TotalPrice().applyEvents(events).totalPrice
        this.cartItems = cartItems
        return this
    }

}


