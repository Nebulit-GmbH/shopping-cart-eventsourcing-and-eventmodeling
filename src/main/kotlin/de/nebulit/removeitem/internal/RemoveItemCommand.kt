package de.nebulit.removeitem.internal

import de.nebulit.common.Command
import java.util.UUID

data class RemoveItemCommand(override var aggregateId:UUID,var cartItemId:UUID) : Command
