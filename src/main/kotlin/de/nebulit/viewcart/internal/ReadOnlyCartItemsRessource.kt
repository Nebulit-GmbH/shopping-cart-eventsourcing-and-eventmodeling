package de.nebulit.viewcart.internal

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.DelegatingQueryHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.common.ReadModel
import de.nebulit.viewcart.CartItemsReadModel
import de.nebulit.viewcart.CartItemsReadModelQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging
import org.springframework.web.bind.annotation.CrossOrigin


@RestController
class ViewcartRessource(
    private var repository: EventsEntityRepository,
    private var delegatingQueryHandler: DelegatingQueryHandler) {

    var logger = KotlinLogging.logger {}

    @CrossOrigin
    @GetMapping("/viewcart")
    fun findInformation(@RequestParam aggregateId:UUID):ReadModel<CartItemsReadModel> {
        return delegatingQueryHandler.handleQuery<UUID, CartItemsReadModel>(CartItemsReadModelQuery(aggregateId))

    }


}
