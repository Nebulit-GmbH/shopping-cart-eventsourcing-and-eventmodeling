package de.nebulit.clearcart.internal

import de.nebulit.common.Command
import java.util.UUID

data class ClearCartCommand(override var aggregateId:UUID) : Command
