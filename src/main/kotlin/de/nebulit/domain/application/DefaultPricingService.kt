package de.nebulit.domain.application

import de.nebulit.common.AggregateService
import de.nebulit.domain.CartAggregate
import de.nebulit.domain.PricingService
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DefaultPricingService(val aggregateService: AggregateService<CartAggregate>): PricingService {
    override fun calculatePrice(aggregateId: UUID): Double {
        val cart =aggregateService.findByAggregateId(aggregateId)
        var totalPrice = 0.0
        cart?.cartItems?.cartItems?.forEach { cartItem -> totalPrice += cartItem.value.price * cartItem.value.quantity }
        return totalPrice
    }


}
