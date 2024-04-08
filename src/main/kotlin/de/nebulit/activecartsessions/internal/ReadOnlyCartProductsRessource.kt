package de.nebulit.activecartsessions.internal

import de.nebulit.activecartsessions.ActiveCartProductsWithProductsReadModel
import de.nebulit.activecartsessions.ActiveCartProductsWithProductsReadModelQuery
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.common.ReadModel
import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging
import org.springframework.web.bind.annotation.CrossOrigin


@RestController
class ActivecartsessionsRessource(
    private var repository: EventsEntityRepository,
    private var cartSessionRepository: CartSessionRepository,
    private var activeCartProductsWithProductsReadModelQueryHandler: ActiveCartProductsWithProductsReadModelQueryHandler
) {

    var logger = KotlinLogging.logger {}

    @CrossOrigin
    @GetMapping("/activecartsessions")
    fun findInformation(@RequestParam aggregateId:UUID, @RequestParam productId:UUID):ReadModel<ActiveCartProductsWithProductsReadModel> {
        return activeCartProductsWithProductsReadModelQueryHandler.handleQuery(
            ActiveCartProductsWithProductsReadModelQuery(productId)
        )
    }


}
