package de.nebulit.domain

import de.nebulit.common.ReadModel
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.events.CartClearedEvent
import de.nebulit.events.CartItemRemovedEvent
import de.nebulit.events.CarttemAddedEvent

class TotalPrice: ReadModel<TotalPrice> {

    var totalPrice = 0.0

    override fun applyEvents(events: List<InternalEvent>): TotalPrice {
        events.forEach {
            when (it.value) {
                is CarttemAddedEvent -> totalPrice+= (it.value as CarttemAddedEvent).price* (it.value as CarttemAddedEvent).quantity
                is CartItemRemovedEvent -> totalPrice-= (it.value as CarttemAddedEvent).price* (it.value as CarttemAddedEvent).quantity
                is CartClearedEvent -> totalPrice = 0.0
            }
        }
        return this
    }
}
