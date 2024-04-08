package de.nebulit.requestedproductrevocations.internal

import de.nebulit.common.AggregateService
import de.nebulit.common.Query
import de.nebulit.common.QueryHandler
import de.nebulit.domain.CartAggregate
import org.springframework.stereotype.Component
import de.nebulit.requestedproductrevocations.RequestedProductRevocationsReadModel
import de.nebulit.requestedproductrevocations.RequestedProductRevocationsReadModelQuery
import java.util.UUID

//TODO assumes can be rebuild from the eventstream
@Component
class RequestedProductRevocationsReadModelQueryHandler(
    var productRevocationRepository: ProductRevocationRepository
) :
    QueryHandler<UUID, RequestedProductRevocationsReadModel> {

    override fun handleQuery(query: Query<UUID>): RequestedProductRevocationsReadModel {
        var productRevocations = productRevocationRepository.findAll()
        return RequestedProductRevocationsReadModel(productRevocations)
    }

    override fun <T> canHandle(query: Query<T>): Boolean {
        return query is RequestedProductRevocationsReadModelQuery
    }

}
