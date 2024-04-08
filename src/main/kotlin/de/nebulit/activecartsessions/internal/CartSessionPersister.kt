package de.nebulit.activecartsessions.internal

import de.nebulit.events.*
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class CartSessionPersister(var cartSessionRepository: CartSessionRepository) {

    @ApplicationModuleListener
    fun sessionStarted(event: CartSessionStartedEvent) {
        cartSessionRepository.save(ActiveCartSession(event.aggregateId))
    }

    @ApplicationModuleListener
    fun cartSubmitted(event: CartSubmittedEvent) {
        cartSessionRepository.deleteById(event.aggregateId)
    }
    //cart expiration not modelled.

    @ApplicationModuleListener
    fun itemRemoved(event: CartItemRemovedEvent) {
        var cart = cartSessionRepository.findById(event.aggregateId).getOrNull()
        if (cart == null) {
            return
        }
        cart.cartItems.removeIf { it.cartItemId == event.cartItemId }
        cartSessionRepository.save(cart)
    }

    @ApplicationModuleListener
    fun cartCleared(event: CartClearedEvent) {
        var cart = cartSessionRepository.findById(event.aggregateId).getOrNull()
        if (cart == null) {
            return
        }
        cart.cartItems.clear()
        cartSessionRepository.save(cart)
    }

    @ApplicationModuleListener
    fun itemAdded(event: CarttemAddedEvent) {
        var cart = cartSessionRepository.findById(event.aggregateId).get()
        cart.cartItems.add(CartItem(event.cartItemId, event.productId))
        cartSessionRepository.save(cart)
    }
}
