package de.nebulit.domain

import java.util.*

interface PricingService {

    fun calculatePrice(aggregateId: UUID): Double
}
