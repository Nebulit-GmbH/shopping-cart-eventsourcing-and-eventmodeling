package de.nebulit.pricechange.internal

import de.nebulit.common.Command
import java.util.*


data class ChangePriceCommand(override var aggregateId: UUID, var oldPrice:Double, var newPrice:Double) : Command
