package de.nebulit.additem

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository
import de.nebulit.common.persistence.InternalEvent


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
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario
import java.util.*

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(ContainerConfiguration::class)
class AddItemaddingmultipleitemsTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("de019507-cf2a-4ec3-9855-a3ee00fedfc6")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `AddItemaddingmultipleitemsTest`(scenario: Scenario) {

        var whenResult = scenario.stimulate { stimulus, eventPublisher ->
            run {
                stimulus.executeWithoutResult {
                    //GIVEN

                    var events = mutableListOf<InternalEvent>()

                    events.add(RandomData.newInstance(listOf("value")) {
                        aggregateId = UUID.fromString("de019507-cf2a-4ec3-9855-a3ee00fedfc6")
                        this.value = CartSessionStartedEvent(
                            aggregateId = UUID.fromString("de019507-cf2a-4ec3-9855-a3ee00fedfc6")
                        )
                    })


                    events.forEach { event ->
                        run {
                            repository.save(event)
                            event.value?.let { eventPublisher.publishEvent(it) }
                        }
                    }


                    //WHEN
                    commandHandler.handle(
                        AddItemCommand(productName = RandomData.newInstance { },
                            price = RandomData.newInstance { },
                            aggregateId = UUID.fromString("de019507-cf2a-4ec3-9855-a3ee00fedfc6"),
                            quantity = RandomData.newInstance { },
                            productimage = RandomData.newInstance { },
                            productId = PRODUCT_ID1)
                    )
                    commandHandler.handle(
                        AddItemCommand(productName = RandomData.newInstance { },
                            price = RandomData.newInstance { },
                            aggregateId = UUID.fromString("de019507-cf2a-4ec3-9855-a3ee00fedfc6"),
                            quantity = RandomData.newInstance { },
                            productimage = RandomData.newInstance { },
                            productId = PRODUCT_ID2)
                    )
                }
            }
        }

        //THEN
        whenResult.andWaitForStateChange {
            var events = repository.findByAggregateId(AGGREGATE_ID)
            events.map { it.value }.filterIsInstance<CarttemAddedEvent>().filter { it.productId == PRODUCT_ID2 || it.productId == PRODUCT_ID1 }.size == 2
        }
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("de019507-cf2a-4ec3-9855-a3ee00fedfc6")
        var PRODUCT_ID1 = UUID.fromString("de019507-cf2a-4ec3-9855-a3ee00fedfc7")
        var PRODUCT_ID2 = UUID.fromString("de019507-cf2a-4ec3-9855-a3ee00fedfc8")
    }

}
