package de.nebulit.domain

import de.nebulit.common.AggregateService
import de.nebulit.common.Query
import de.nebulit.common.QueryHandler
import de.nebulit.common.ReadModel
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.events.*
import java.util.UUID
import mu.KotlinLogging
import org.springframework.stereotype.Component

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

                is CartItemRemovedEvent -> {
                    cartItems.remove((it.value as CartItemRemovedEvent).cartItemId)
                }

                is CartClearedEvent -> {
                    cartItems.clear()
                }

                is ProductRevokedEvent -> {
                    cartItems =
                        cartItems.filter { item -> item.value.productId !== (it.value as ProductRevokedEvent).productId }
                            .toMutableMap()
                }
            }
        }
        this.cartItems = cartItems
        return this
    }

}



