package de.nebulit.activecartsessions

import de.nebulit.activecartsessions.internal.CartSessionRepository
import de.nebulit.common.Query
import de.nebulit.common.QueryHandler
import de.nebulit.common.ReadModel
import de.nebulit.common.persistence.InternalEvent
import java.util.UUID
import mu.KotlinLogging
import org.springframework.stereotype.Component

class ActiveCartProductsWithProductsReadModelQuery(var changedProductId: UUID) : Query<UUID> {
    override fun toParam(): UUID {
        return changedProductId
    }

}


class ActiveCartProductsWithProductsReadModel(var productId:UUID, var cartIds: List<UUID>) : ReadModel<ActiveCartProductsWithProductsReadModel> {

    var logger = KotlinLogging.logger {}


    override fun applyEvents(events: List<InternalEvent>): ActiveCartProductsWithProductsReadModel {
        //no op
        return this
    }

}



