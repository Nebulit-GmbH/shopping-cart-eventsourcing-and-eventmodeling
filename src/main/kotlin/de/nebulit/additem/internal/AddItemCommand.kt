package de.nebulit.additem.internal

import de.nebulit.common.Command
import java.util.UUID

data class AddItemCommand(var productName:String,var price:Double,override var aggregateId:UUID,var quantity:Int,var productimage:String,var productId:UUID) : Command
