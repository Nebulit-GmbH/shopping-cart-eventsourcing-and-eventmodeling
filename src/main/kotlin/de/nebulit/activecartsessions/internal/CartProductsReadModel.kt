package de.nebulit.activecartsessions.internal

import de.nebulit.common.Query
import de.nebulit.common.QueryHandler
import de.nebulit.common.ReadModel
import de.nebulit.common.persistence.InternalEvent
import java.util.UUID
import mu.KotlinLogging
import org.springframework.stereotype.Component

class CartProductsReadModelQuery(var productIds: List<UUID>) : Query<List<UUID>> {
    override fun toParam(): List<UUID> {
        return productIds.toList()
    }

}

@Component
class CartProductsReadModelQueryHandler(private var cartSessionRepository: CartSessionRepository) :
    QueryHandler<List<UUID>, CartProductsReadModel> {
    override fun handleQuery(query: Query<List<UUID>>): CartProductsReadModel {
        val queryResult = cartSessionRepository.findByProductId(query.toParam()).map { it.cartId }
        return CartProductsReadModel(queryResult)
    }

    override fun <T> canHandle(query: Query<T>): Boolean {
        return query is CartProductsReadModelQuery
    }

}

class CartProductsReadModel(var data: List<UUID>) : ReadModel<CartProductsReadModel> {

    var logger = KotlinLogging.logger {}


    override fun applyEvents(events: List<InternalEvent>): CartProductsReadModel {
        //no op
        return this
    }

}



