package de.nebulit.removeitem

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository

import de.nebulit.events.CarttemAddedEvent;
import de.nebulit.removeitem.internal.RemoveItemCommand;
import de.nebulit.events.CartItemRemovedEvent
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import de.nebulit.common.support.RandomData
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario
import java.util.*

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(ContainerConfiguration::class)
class RemoveitemTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("152e52f5-f608-4209-9df8-dedbe19fd084")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `RemoveitemTest`(scenario: Scenario) {

        //GIVEN

        val cartItemId = RandomData.newInstance<UUID> { }
        repository.save(RandomData.newInstance(listOf("value")) {
            aggregateId = UUID.fromString("152e52f5-f608-4209-9df8-dedbe19fd084")
            this.value = CarttemAddedEvent(
                productName = RandomData.newInstance { },
                price = RandomData.newInstance { },
                quantity = RandomData.newInstance { },
                productimage = RandomData.newInstance { },
                aggregateId = UUID.fromString("152e52f5-f608-4209-9df8-dedbe19fd084"),
                cartItemId = cartItemId,
                productId = RandomData.newInstance { }
            )
        })


        repository.save(RandomData.newInstance(listOf("value")) {
            aggregateId = UUID.fromString("152e52f5-f608-4209-9df8-dedbe19fd084")
            this.value = CarttemAddedEvent(
                productName = RandomData.newInstance { },
                price = RandomData.newInstance { },
                quantity = RandomData.newInstance { },
                productimage = RandomData.newInstance { },
                aggregateId = UUID.fromString("152e52f5-f608-4209-9df8-dedbe19fd084"),
                cartItemId = RandomData.newInstance { },
                productId = RandomData.newInstance { }
            )
        })


        //WHEN
        var whenResult = scenario.stimulate { _ ->
            commandHandler.handle(
                RemoveItemCommand(aggregateId = UUID.fromString("152e52f5-f608-4209-9df8-dedbe19fd084"),
                    cartItemId = cartItemId)
            )
        }

        //THEN
        whenResult.andWaitForEventOfType(CartItemRemovedEvent::class.java)
            .toArrive()
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("152e52f5-f608-4209-9df8-dedbe19fd084")
    }

}
