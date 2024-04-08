package de.nebulit.inventory.internal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


@Entity
@Table(name = "inventories")
class Inventory(@Id @Column(name="inventory_id") var inventoryId: UUID, var inventory: Int = 0)

interface InventoryRepository : JpaRepository<Inventory, UUID> {
    fun findAllByInventoryIdIn(inventoryIds: List<UUID>): List<Inventory>
}
