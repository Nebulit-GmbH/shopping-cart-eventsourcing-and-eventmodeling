package de.nebulit.inventory.internal

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.inventory.internal.ProductInventoryReadModel
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.common.ReadModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging


@RestController
class InventoryRessource(private var repository: EventsEntityRepository,
    private var inventoryRepository: InventoryRepository) {

    var logger = KotlinLogging.logger {}

    @GetMapping("/inventory")
    fun findInformation(@RequestParam aggregateId:UUID):ReadModel<ProductInventoryReadModel> {
        var result = ProductInventoryReadModel(inventoryRepository).applyEvents(repository.findByAggregateId(aggregateId))
        return result
    }


}
