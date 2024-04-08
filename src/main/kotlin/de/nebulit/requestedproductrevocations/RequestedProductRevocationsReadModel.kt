package de.nebulit.requestedproductrevocations

import de.nebulit.common.Query
import de.nebulit.common.ReadModel
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.requestedproductrevocations.internal.ProductRevocation
import java.util.UUID
import mu.KotlinLogging

//TODO adjust the query to the necessary parameters
class RequestedProductRevocationsReadModelQuery() : Query<Unit> {
    override fun toParam() {
    }
}

class RequestedProductRevocationsReadModel(val productRevocations: List<ProductRevocation>) : ReadModel<RequestedProductRevocationsReadModel> {

    var logger = KotlinLogging.logger {}


    override fun applyEvents(events: List<InternalEvent>): RequestedProductRevocationsReadModel {
       //no op
        return this
    }

}

