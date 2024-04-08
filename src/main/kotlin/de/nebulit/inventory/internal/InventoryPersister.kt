package de.nebulit.inventory.internal

import de.nebulit.events.InventoryChangedEvent
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class InventoryPersister(var inventoryRepository: InventoryRepository) {

    @ApplicationModuleListener
    fun inventoryChanged(event: InventoryChangedEvent) {
        inventoryRepository.save(Inventory(event.aggregateId, event.quantity))
    }
}
