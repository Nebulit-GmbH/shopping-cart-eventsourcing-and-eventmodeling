package de.nebulit.requestproductrevoke.internal

import de.nebulit.common.Command
import java.util.UUID

data class RequestProductRevocationAfterPriceChangeCommand(override var aggregateId:UUID, var productId:UUID) : Command
