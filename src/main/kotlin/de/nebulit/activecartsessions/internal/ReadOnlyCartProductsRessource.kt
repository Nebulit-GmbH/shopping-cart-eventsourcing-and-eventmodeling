package de.nebulit.activecartsessions.internal

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.activecartsessions.internal.CartProductsReadModel
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.common.ReadModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging


@RestController
class ActivecartsessionsRessource(
    private var repository: EventsEntityRepository,
    private var cartSessionRepository: CartSessionRepository,
) {

    var logger = KotlinLogging.logger {}

    @GetMapping("/activecartsessions")
    fun findInformation(@RequestParam aggregateId:UUID, @RequestParam productId:UUID):ReadModel<CartProductsReadModel> {
        return CartProductsReadModel(listOf(productId)).applyEvents(repository.findByAggregateId(aggregateId))

    }


}
