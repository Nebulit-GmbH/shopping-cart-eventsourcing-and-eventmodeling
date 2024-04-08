package de.nebulit.activecartsessions.internal

import de.nebulit.activecartsessions.ActiveCartProductsWithProductsReadModel
import de.nebulit.activecartsessions.ActiveCartProductsWithProductsReadModelQuery
import de.nebulit.common.Query
import de.nebulit.common.QueryHandler
import org.springframework.stereotype.Component
import java.util.*

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
