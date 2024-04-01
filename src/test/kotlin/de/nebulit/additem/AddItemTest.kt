package de.nebulit.additem

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository

import de.nebulit.additem.internal.AddItemCommand;
import de.nebulit.events.CartSessionStartedEvent;
import de.nebulit.events.CarttemAddedEvent
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
class AddItemTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun `AddItemTest`(scenario: Scenario) {

        //GIVEN


        //WHEN
        var whenResult = scenario.stimulate { _ ->
            commandHandler.handle(
                AddItemCommand(productName = RandomData.newInstance { },
                    price = RandomData.newInstance { },
                    aggregateId = UUID.fromString("278881ce-15a8-42bb-94fa-c55e58647cd9"),
                    quantity = RandomData.newInstance { },
                    productimage = RandomData.newInstance { },
                    productId = RandomData.newInstance { })
            )
        }

        //THEN
        whenResult.andWaitForEventOfType(CartSessionStartedEvent::class.java)
            .toArrive()
        whenResult.andWaitForEventOfType(CarttemAddedEvent::class.java)
            .toArrive()
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("278881ce-15a8-42bb-94fa-c55e58647cd9")
    }

}
