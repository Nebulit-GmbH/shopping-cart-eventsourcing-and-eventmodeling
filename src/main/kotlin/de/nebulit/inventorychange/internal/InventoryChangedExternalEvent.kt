package de.nebulit.inventorychange.internal

import de.nebulit.common.Event
import java.util.*

data class InventoryChangedEventExternal(var productId: UUID, var quantity:Int) : Event

