package de.nebulit.inventory.internal

import de.nebulit.common.DelegatingQueryHandler
import de.nebulit.common.ReadModel
import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging
import org.springframework.web.bind.annotation.CrossOrigin


@RestController
class InventoryRessource(
    var delegatingQueryHandler: DelegatingQueryHandler
) {

    var logger = KotlinLogging.logger {}

    @CrossOrigin
    @GetMapping("/inventory")
    fun findInformation(@RequestParam aggregateId: UUID): ProductInventoryReadModel {
        return delegatingQueryHandler.handleQuery(
            ProductInventoryReadModelQuery(
                aggregateId
            )
        )
    }


}
