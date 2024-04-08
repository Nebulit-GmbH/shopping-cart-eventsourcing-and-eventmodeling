package de.nebulit.activecartsessions.internal

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

@Component
class ActiveCartProductsWithProductsReadModelQueryHandler(private var cartSessionRepository: CartSessionRepository) :
    QueryHandler<UUID, ActiveCartProductsWithProductsReadModel> {
    override fun handleQuery(query: Query<UUID>): ActiveCartProductsWithProductsReadModel {
        val activeCartsContainingProduct = cartSessionRepository.findByProductId(listOf( query.toParam())).map { it.cartId }
        return ActiveCartProductsWithProductsReadModel(query.toParam(), activeCartsContainingProduct)
    }

    override fun <T> canHandle(query: Query<T>): Boolean {
        return query is ActiveCartProductsWithProductsReadModelQuery
    }

}

class ActiveCartProductsWithProductsReadModel(var productId:UUID, var cartIds: List<UUID>) : ReadModel<ActiveCartProductsWithProductsReadModel> {

    var logger = KotlinLogging.logger {}


    override fun applyEvents(events: List<InternalEvent>): ActiveCartProductsWithProductsReadModel {
        //no op
        return this
    }

}



