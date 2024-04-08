package de.nebulit.requestedproductrevocations.internal

import de.nebulit.common.DelegatingQueryHandler
import de.nebulit.requestedproductrevocations.RequestedProductRevocationsReadModel
import de.nebulit.requestedproductrevocations.RequestedProductRevocationsReadModelQuery
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.common.ReadModel
import org.springframework.web.bind.annotation.GetMapping



import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging


@RestController
class RequestedproductexpirationsRessource(
    private var repository: EventsEntityRepository,
    private var delegatingQueryHandler: DelegatingQueryHandler
    ) {

    var logger = KotlinLogging.logger {}

    @GetMapping("/requestedproductexpirations")
    fun findInformation(@RequestParam aggregateId:UUID):ReadModel<RequestedProductRevocationsReadModel> {
    return delegatingQueryHandler.handleQuery<Unit, RequestedProductRevocationsReadModel>(RequestedProductRevocationsReadModelQuery())    }


}
