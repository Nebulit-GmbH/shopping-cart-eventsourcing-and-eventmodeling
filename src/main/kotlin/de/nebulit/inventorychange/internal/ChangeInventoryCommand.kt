package de.nebulit.inventorychange.internal

import de.nebulit.common.Command
import java.util.UUID


data class ChangeInventoryCommand(override var aggregateId: UUID,var quantity:Int) : Command
