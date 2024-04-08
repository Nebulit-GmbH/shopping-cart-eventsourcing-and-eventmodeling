package de.nebulit.inventory.internal

import com.fasterxml.jackson.annotation.JsonIgnore
import de.nebulit.common.ReadModel
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.domain.TotalPrice
import de.nebulit.events.CartClearedEvent
import de.nebulit.events.CartItemRemovedEvent
import de.nebulit.events.CarttemAddedEvent
import de.nebulit.events.InventoryChangedEvent
import de.nebulit.viewcart.internal.CartItem
import java.util.*
import java.util.UUID
import mu.KotlinLogging


class ProductInventoryReadModel(@field:JsonIgnore var inventoryRepository: InventoryRepository) : ReadModel<ProductInventoryReadModel> {

    @JsonIgnore
    var logger = KotlinLogging.logger {}

    var inventoryMap = emptyMap<UUID,Int>()


    override fun applyEvents(events: List<InternalEvent>): ProductInventoryReadModel {
        var productIds = mutableListOf<UUID>()
        events.forEach {
            when (it.value) {
                is CarttemAddedEvent -> {
                    productIds.add((it.value as CarttemAddedEvent).productId)
                }

                is CartItemRemovedEvent -> {
                    productIds.remove((it.value as CartItemRemovedEvent).cartItemId)
                }

                is CartClearedEvent -> productIds.clear()
            }
        }
        this.inventoryMap = inventoryRepository.findAllByInventoryIdIn(productIds).groupBy(Inventory::inventoryId)
            .mapValues { entry->entry.value.lastOrNull()?.inventory?:0}

        return this
    }

}



