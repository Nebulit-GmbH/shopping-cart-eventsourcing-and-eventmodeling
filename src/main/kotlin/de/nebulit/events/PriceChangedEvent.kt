package de.nebulit.events

import de.nebulit.common.Event

import mu.KotlinLogging
import java.util.*


data class PriceChangedEvent(var aggregateId: UUID, var oldPrice:Double, var newPrice:Double) : Event
