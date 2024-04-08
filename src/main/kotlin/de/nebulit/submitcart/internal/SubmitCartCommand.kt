package de.nebulit.submitcart.internal

import de.nebulit.common.Command
import java.util.UUID

data class SubmitCartCommand(override var aggregateId:UUID) : Command
