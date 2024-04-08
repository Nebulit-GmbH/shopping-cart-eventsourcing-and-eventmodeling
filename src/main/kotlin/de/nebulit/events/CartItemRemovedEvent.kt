package de.nebulit.events

import de.nebulit.common.Event
import java.util.UUID
import mu.KotlinLogging


data class CartItemRemovedEvent(var aggregateId:UUID,var cartItemId:UUID) : Event
