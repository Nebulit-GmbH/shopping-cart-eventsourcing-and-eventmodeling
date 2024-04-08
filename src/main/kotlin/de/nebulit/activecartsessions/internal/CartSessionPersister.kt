package de.nebulit.activecartsessions.internal

import de.nebulit.events.*
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener
import kotlin.jvm.optionals.getOrNull

@Component
class CartSessionPersister(var cartSessionRepository: CartSessionRepository) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    fun sessionStarted(event: CartSessionStartedEvent) {
        cartSessionRepository.save(ActiveCartSession(event.aggregateId))
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    fun cartSubmitted(event: CartSubmittedEvent) {
        cartSessionRepository.deleteById(event.aggregateId)
    }
    //cart expiration not modelled.

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    fun itemRemoved(event: CartItemRemovedEvent) {
        var cart = cartSessionRepository.findById(event.aggregateId).getOrNull()
        if (cart == null) {
            return
        }
        cart.cartItems.removeIf { it.cartItemId == event.cartItemId }
        cartSessionRepository.save(cart)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    fun cartCleared(event: CartClearedEvent) {
        var cart = cartSessionRepository.findById(event.aggregateId).getOrNull()
        if (cart == null) {
            return
        }
        cart.cartItems.clear()
        cartSessionRepository.save(cart)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    fun itemAdded(event: CarttemAddedEvent) {
        var cart = cartSessionRepository.findById(event.aggregateId).get()
        cart.cartItems.add(CartItem(event.cartItemId, event.productId))
        cartSessionRepository.save(cart)
    }
}
