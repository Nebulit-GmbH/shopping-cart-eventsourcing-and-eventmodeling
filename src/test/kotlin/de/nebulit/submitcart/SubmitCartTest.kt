package de.nebulit.submitcart

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository

import de.nebulit.events.CarttemAddedEvent;
import de.nebulit.submitcart.internal.SubmitCartCommand;
import de.nebulit.events.CartSubmittedEvent
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
class SubmitCartTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("7c980847-2514-4ee0-b8d5-feb7517b1d0f")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `SubmitCartTest`(scenario: Scenario) {

        //GIVEN

        repository.save(RandomData.newInstance(listOf("value")) {
            aggregateId = UUID.fromString("7c980847-2514-4ee0-b8d5-feb7517b1d0f")
            this.value = CarttemAddedEvent(
                productName = RandomData.newInstance { },
                price = RandomData.newInstance { },
                quantity = RandomData.newInstance { },
                productimage = RandomData.newInstance { },
                aggregateId = UUID.fromString("7c980847-2514-4ee0-b8d5-feb7517b1d0f"),
                cartItemId = RandomData.newInstance { },
                productId = RandomData.newInstance { }
            )
        })


        //WHEN
        var whenResult = scenario.stimulate { _ ->
            commandHandler.handle(
                SubmitCartCommand(aggregateId = UUID.fromString("7c980847-2514-4ee0-b8d5-feb7517b1d0f"))
            )
        }

        //THEN
        whenResult.andWaitForEventOfType(CartSubmittedEvent::class.java)
            .matchingMapped({event -> event.cartItems}, {items -> items.size == 1})
            .toArrive()
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("7c980847-2514-4ee0-b8d5-feb7517b1d0f")
    }

}
