package de.nebulit.domain

import de.nebulit.common.AggregateRoot
import de.nebulit.common.CommandException
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.events.CartItemRemovedEvent
import de.nebulit.events.CartSessionStartedEvent
import de.nebulit.events.CarttemAddedEvent
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import java.sql.Types
import java.util.*
import kotlin.jvm.Transient

data class CartItem(
    var productName: String,
    var price: Double,
    var quantity: Int,
    var productimage: String,
    var productId: UUID
)

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
    private var cartItems = mutableListOf<UUID>()

    @Transient
    override var events: MutableList<InternalEvent> = mutableListOf()

    override fun applyEvents(events: List<InternalEvent>): AggregateRoot {
        events.forEach {
            when (it.value) {
                is CarttemAddedEvent -> cartItems.add((it.value as CarttemAddedEvent).cartItemId)
                is CartItemRemovedEvent -> cartItems.remove((it.value as CartItemRemovedEvent).cartItemId)
            }
        }
        return this
    }

    private fun handleRemove(event: CartItemRemovedEvent) {


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
        if (!cartItems.contains(cartItemId)) {
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

    companion object {
        fun newSession(aggregateId: UUID): CartAggregate {
            val aggregate = CartAggregate(aggregateId)
            aggregate.newSession()
            return aggregate
        }
    }


}
