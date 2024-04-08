package de.nebulit.domain

import de.nebulit.common.AggregateRoot
import de.nebulit.common.CommandException
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.events.InventoryChangedEvent
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import java.sql.Types
import java.time.LocalDate
import java.util.*
import kotlin.jvm.Transient

@Entity
@Table(name = "aggregates")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Discriminator", discriminatorType = DiscriminatorType.STRING, length = 20)
@DiscriminatorValue("ProductInventoryAggregate")
class ProductInventoryAggregate(
    @JdbcTypeCode(Types.VARCHAR) @Id override var aggregateId: UUID
) : AggregateRoot {

    override var version: Long? = 0

    @Transient
    override var events: MutableList<InternalEvent> = mutableListOf()

    @Transient
    private var inventory:Int = 0

    override fun applyEvents(events: List<InternalEvent>): AggregateRoot {
        events.forEach {
            when (it.value) {
                is InventoryChangedEvent -> this.inventory = (it.value as InventoryChangedEvent).quantity
            }
        }
        return this
    }

    fun inventoryChanged(quantity: Int) {
        if (quantity < 0) {
            throw CommandException("invalid quantity")
        }
        events.add(InternalEvent().apply {
            this.aggregateId = this@ProductInventoryAggregate.aggregateId
            this.value = InventoryChangedEvent(
                this@ProductInventoryAggregate.aggregateId,
                quantity
            )
        })
    }

    companion object {
    }
}
