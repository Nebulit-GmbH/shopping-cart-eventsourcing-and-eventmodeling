package de.nebulit.revokeproductafterpricechange.internal

import de.nebulit.common.Command
import java.util.UUID

data class RevokeProductAfterPriceChangeCommand(override var aggregateId:UUID,var productId:UUID) : Command
