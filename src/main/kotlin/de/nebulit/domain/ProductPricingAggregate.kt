package de.nebulit.domain

import de.nebulit.common.AggregateRoot
import de.nebulit.common.CommandException
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.events.PriceChangedEvent
import de.nebulit.pricechange.internal.ChangePriceCommand
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
@DiscriminatorValue("ProductPricingAggregate")
class ProductPricingAggregate(
    @JdbcTypeCode(Types.VARCHAR) @Id override var aggregateId: UUID
) : AggregateRoot {

    override var version: Long? = 0

    @Transient
    override var events: MutableList<InternalEvent> = mutableListOf()

    override fun applyEvents(events: List<InternalEvent>): AggregateRoot {
        return this
    }

    fun priceChanged(oldPrice: Double, price:Double) {
        if (price <= 0) {
            throw CommandException("cannot set negative or zero Price")
        }
        this.events.add(InternalEvent().apply {
            this.aggregateId=this@ProductPricingAggregate.aggregateId
            this.value = PriceChangedEvent(this@ProductPricingAggregate.aggregateId,oldPrice, price)
        })
    }

    companion object {
    }
}
