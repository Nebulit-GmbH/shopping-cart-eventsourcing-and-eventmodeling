package de.nebulit.inventory

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.DelegatingQueryHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.ProductInventoryAggregateRepository

import de.nebulit.events.CartSessionStartedEvent;
import de.nebulit.events.CarttemAddedEvent;
import de.nebulit.events.InventoryChangedEvent;
import de.nebulit.inventory.internal.ProductInventoryReadModel
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import de.nebulit.common.support.RandomData
import de.nebulit.inventory.internal.InventoryRepository
import de.nebulit.inventory.internal.ProductInventoryReadModelQuery
import de.nebulit.inventorychange.internal.InventoryChangedEventExternal
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario
import org.springframework.transaction.annotation.Transactional
import java.util.*

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(ContainerConfiguration::class)
class InventoryTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var inventoryRepository: InventoryRepository

    @Autowired
    lateinit var aggregateRepository: ProductInventoryAggregateRepository

    @Autowired
    lateinit var delegatingQueryHandler: DelegatingQueryHandler

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("890a836f-9cbd-43f4-a6b2-dbbd110f862e")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `InventoryTest`(scenario: Scenario) {

        //GIVEN

        repository.save(RandomData.newInstance(listOf("value")) {
            aggregateId = UUID.fromString("890a836f-9cbd-43f4-a6b2-dbbd110f862e")
            this.value = CartSessionStartedEvent(
                aggregateId = UUID.fromString("890a836f-9cbd-43f4-a6b2-dbbd110f862e")
            )
        })


        repository.save(RandomData.newInstance(listOf("value")) {
            aggregateId = UUID.fromString("890a836f-9cbd-43f4-a6b2-dbbd110f862e")
            this.value = CarttemAddedEvent(
                productName = RandomData.newInstance { },
                price = RandomData.newInstance { },
                quantity = RandomData.newInstance { },
                productimage = RandomData.newInstance { },
                aggregateId = UUID.fromString("890a836f-9cbd-43f4-a6b2-dbbd110f862e"),
                cartItemId = RandomData.newInstance { },
                productId = UUID.fromString("890a836f-9cbd-43f4-a6b2-dbbd110f862e")
            )
        })

        var inventoryChangedEvent = InventoryChangedEvent(
            aggregateId = UUID.fromString("890a836f-9cbd-43f4-a6b2-dbbd110f862e"),
            quantity = 13
        )
        repository.save(RandomData.newInstance(listOf("value")) {
            aggregateId = UUID.fromString("890a836f-9cbd-43f4-a6b2-dbbd110f862e")
            this.value = inventoryChangedEvent
        })

        //THEN

        var whenResult = scenario.publish(inventoryChangedEvent)

        whenResult.andWaitForStateChange {

            var readModel: ProductInventoryReadModel = delegatingQueryHandler.handleQuery(ProductInventoryReadModelQuery(AGGREGATE_ID))

            readModel.inventoryMap[AGGREGATE_ID] == 13 }
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("890a836f-9cbd-43f4-a6b2-dbbd110f862e")
    }

}
