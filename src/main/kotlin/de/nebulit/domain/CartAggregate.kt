package de.nebulit.domain

import de.nebulit.common.AggregateRoot
import de.nebulit.common.CommandException
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.events.*
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import java.sql.Types
import java.util.*
import kotlin.jvm.Transient


@Entity
@Table(name = "aggregates")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Discriminator", discriminatorType = DiscriminatorType.STRING, length = 20)
@DiscriminatorValue("CartAggregate")
class CartAggregate(
    @JdbcTypeCode(Types.VARCHAR) @Id override var aggregateId: UUID
) : AggregateRoot {

    override var version: Long? = 0

    @Transient
    private var cartItemIds = mutableListOf<UUID>()

    @Transient
    override var events: MutableList<InternalEvent> = mutableListOf()

    @Transient
    private var cartItems = CartItems()

    @Transient
    private var totalPrice = TotalPrice()

    override fun applyEvents(events: List<InternalEvent>): AggregateRoot {
        cartItems.applyEvents(events)
        totalPrice.applyEvents(events)
        return this
    }



    fun newSession() {
        this.events.add(InternalEvent().apply {
            this.aggregateId = this@CartAggregate.aggregateId
            this.value = CartSessionStartedEvent(this@CartAggregate.aggregateId)
        })
    }

    fun addItem(cartItem: CartItem) {
        this.events.add(InternalEvent().apply {
            this.aggregateId = this@CartAggregate.aggregateId
            this.value = CarttemAddedEvent(
                cartItem.productName,
                cartItem.price,
                cartItem.quantity,
                cartItem.productimage,
                UUID.randomUUID(),
                this@CartAggregate.aggregateId,
                cartItem.productId
            )

        })
    }

    fun removeItem(cartItemId: UUID) {
        if (!cartItems.cartItems.values.map(CartItem::cartItemId).contains(cartItemId)) {
            throw CommandException("item not present")
        }
        this.events.add(InternalEvent().apply {
            this.aggregateId = this@CartAggregate.aggregateId
            this.value = CartItemRemovedEvent(
                this@CartAggregate.aggregateId,
                cartItemId
            )
        })

    }

    fun clear() {
        this.events.add(InternalEvent().apply {
            this.aggregateId = this@CartAggregate.aggregateId
            this.value = CartClearedEvent(
                this@CartAggregate.aggregateId,
            )
        })
    }

    fun submit() {
        if (cartItems.cartItems.isEmpty()) {
            throw CommandException("cannot submit empty cart")
        }
        events.add(InternalEvent().apply {
            this.aggregateId = this@CartAggregate.aggregateId
            this.value = CartSubmittedEvent(
                aggregateId = this@CartAggregate.aggregateId,
                totalPrice = totalPrice.totalPrice,
                cartItems = cartItems.cartItems.values.toList()
            )
        })
    }

    companion object {
        fun newSession(aggregateId: UUID): CartAggregate {
            val aggregate = CartAggregate(aggregateId)
            aggregate.newSession()
            return aggregate
        }
    }


}
