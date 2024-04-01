package de.nebulit.additem

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository

import de.nebulit.events.CartSessionStartedEvent;
import de.nebulit.additem.internal.AddItemCommand;
import de.nebulit.events.CarttemAddedEvent
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import de.nebulit.common.support.RandomData
import org.assertj.core.api.Assertions.assertThat
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario
import java.util.*

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(ContainerConfiguration::class)
class AddItem_AlreadyExistingSessionTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("d4929464-b640-4ea3-bb7b-8be0fc05d26f")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `AddItem_AlreadyExistingSessionTest`(scenario: Scenario) {

        //GIVEN

        repository.save(RandomData.newInstance(listOf("value")) {
            aggregateId = UUID.fromString("d4929464-b640-4ea3-bb7b-8be0fc05d26f")
            this.value = CartSessionStartedEvent(
                aggregateId = UUID.fromString("d4929464-b640-4ea3-bb7b-8be0fc05d26f")
            )
        })


        //WHEN
        var whenResult = scenario.stimulate { _ ->
            commandHandler.handle(
                AddItemCommand(productName = RandomData.newInstance { },
                    price = RandomData.newInstance { },
                    aggregateId = UUID.fromString("d4929464-b640-4ea3-bb7b-8be0fc05d26f"),
                    quantity = RandomData.newInstance { },
                    productimage = RandomData.newInstance { },
                    productId = RandomData.newInstance { })
            )
        }

        //THEN
        whenResult.andWaitForEventOfType(CarttemAddedEvent::class.java)
            .toArrive()

    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("d4929464-b640-4ea3-bb7b-8be0fc05d26f")
    }

}
