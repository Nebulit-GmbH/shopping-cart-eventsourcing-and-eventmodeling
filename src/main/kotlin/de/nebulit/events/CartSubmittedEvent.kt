package de.nebulit.events

import de.nebulit.common.Event
import java.util.UUID
import mu.KotlinLogging

data class CartItem(
    var cartItemId: UUID,
    var productId: UUID,
    var productName: String,
    var price: Double,
    var quantity: Int,
    var productimage: String,
)

data class CartSubmittedEvent(var aggregateId:UUID,var totalPrice:Double,var cartItems:List<CartItem>) : Event
