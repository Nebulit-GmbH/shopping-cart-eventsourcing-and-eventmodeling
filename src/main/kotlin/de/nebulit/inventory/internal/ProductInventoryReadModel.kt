package de.nebulit.inventory.internal

import com.fasterxml.jackson.annotation.JsonIgnore
import de.nebulit.common.AggregateService
import de.nebulit.common.Query
import de.nebulit.common.QueryHandler
import de.nebulit.common.ReadModel
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.domain.CartAggregate
import de.nebulit.events.CartClearedEvent
import de.nebulit.events.CartItemRemovedEvent
import de.nebulit.events.CarttemAddedEvent
import java.util.UUID
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class ProductInventoryReadModelQueryResolver(
    var aggregateService: AggregateService<CartAggregate>,
    var inventoryRepository: InventoryRepository
) :
    QueryHandler<UUID, ProductInventoryReadModel> {
    override fun handleQuery(query: Query<UUID>): ProductInventoryReadModel {
        //find cartItems in cart
        val cartItems = CartItemsReadModel().applyEvents(aggregateService.findEventsByAggregateId(query.toParam()))

        //load inventory for cart items in cart
        val inventoryMap =
            inventoryRepository.findAllByInventoryIdIn(cartItems.productIds).groupBy(Inventory::inventoryId)
                .mapValues { entry -> entry.value.lastOrNull()?.inventory ?: 0 }

        return ProductInventoryReadModel(inventoryMap)
    }

    override fun <T> canHandle(query: Query<T>): Boolean {
        return query is ProductInventoryReadModelQuery
    }

}

class ProductInventoryReadModelQuery(var aggregateId: UUID) : Query<UUID> {
    override fun toParam(): UUID {
        return aggregateId
    }
}


class CartItemsReadModel : ReadModel<CartItemsReadModel> {

    var productIds = listOf<UUID>()
    override fun applyEvents(events: List<InternalEvent>): CartItemsReadModel {
        val productIds = mutableListOf<UUID>()
        events.forEach {
            when (it.value) {
                is CarttemAddedEvent -> {
                    productIds.add((it.value as CarttemAddedEvent).productId)
                }

                is CartItemRemovedEvent -> {
                    productIds.remove((it.value as CartItemRemovedEvent).cartItemId)
                }

                is CartClearedEvent -> productIds.clear()
            }
        }
        this.productIds = productIds
        return this
    }

}

class ProductInventoryReadModel(var inventoryMap: Map<UUID, Int>) : ReadModel<ProductInventoryReadModel> {

    @JsonIgnore
    var logger = KotlinLogging.logger {}

    override fun applyEvents(events: List<InternalEvent>): ProductInventoryReadModel {
        //no op
        return this
    }

}



