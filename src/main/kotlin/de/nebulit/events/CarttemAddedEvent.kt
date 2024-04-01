package de.nebulit.events

import de.nebulit.common.Event
import java.util.UUID
import mu.KotlinLogging


data class CarttemAddedEvent(var productName:String,var price:Double,var quantity:String,var productimage:String,var aggregateId:UUID,var cartItemId:UUID,var productId:UUID) : Event
