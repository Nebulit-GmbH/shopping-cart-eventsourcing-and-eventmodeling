package de.nebulit.events

import de.nebulit.common.Event
import java.util.UUID
import mu.KotlinLogging


data class InventoryChangedEvent(var aggregateId:UUID,var quantity:Int) : Event
