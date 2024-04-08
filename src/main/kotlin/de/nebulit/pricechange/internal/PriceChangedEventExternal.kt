package de.nebulit.pricechange.internal

import java.util.*

class PriceChangedEventExternal(val aggregateId: UUID, val oldPrice: Double, val newPrice: Double)
