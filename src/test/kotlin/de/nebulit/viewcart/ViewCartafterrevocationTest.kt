package de.nebulit.viewcart

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.DelegatingQueryHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository
import de.nebulit.common.persistence.InternalEvent


import de.nebulit.events.CartSessionStartedEvent;
import de.nebulit.events.CarttemAddedEvent;
import de.nebulit.events.ProductRevokedEvent;
import de.nebulit.viewcart.CartItemsReadModel
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import de.nebulit.common.support.RandomData
import org.assertj.core.api.Assertions.assertThat
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario
import java.util.*

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(ContainerConfiguration::class)
class ViewCartafterrevocationTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var queryHandler: DelegatingQueryHandler

    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = AGGREGATE_ID
            this.events = mutableListOf()
        })
    }

    @Test
    fun `ViewCartafterrevocationTest`(scenario: Scenario) {

        var whenResult = prepare(scenario)

        //THEN
        whenResult.andWaitForStateChange {
            var readModel: CartItemsReadModel =
                queryHandler.handleQuery(CartItemsReadModelQuery(AGGREGATE_ID))
            assertThat(readModel.cartItems).isEmpty()
            //assertThat(readModel.data).contains(..)
        }
    }

    private fun prepare(scenario: Scenario): Scenario.When<Void> {
        return scenario.stimulate { stimulus, eventPublisher ->
            run {
                stimulus.executeWithoutResult {
                    //GIVEN

                    var events = mutableListOf<InternalEvent>()

                    events.add(RandomData.newInstance(listOf("value")) {
                        aggregateId = UUID.fromString("9cc42b15-6f27-423e-85f0-4fd24d4846ca")
                        this.value = CartSessionStartedEvent(
                            aggregateId = UUID.fromString("9cc42b15-6f27-423e-85f0-4fd24d4846ca")
                        )
                    })


                    events.add(RandomData.newInstance(listOf("value")) {
                        aggregateId = UUID.fromString("9cc42b15-6f27-423e-85f0-4fd24d4846ca")
                        this.value = CarttemAddedEvent(
                            productName = RandomData.newInstance { },
                            price = RandomData.newInstance { },
                            quantity = RandomData.newInstance { },
                            productimage = RandomData.newInstance { },
                            aggregateId = UUID.fromString("9cc42b15-6f27-423e-85f0-4fd24d4846ca"),
                            cartItemId = RandomData.newInstance { },
                            productId = PRODUCT_ID
                        )
                    })


                    events.add(RandomData.newInstance(listOf("value")) {
                        aggregateId = UUID.fromString("9cc42b15-6f27-423e-85f0-4fd24d4846ca")
                        this.value = ProductRevokedEvent(
                            productId = PRODUCT_ID,
                            aggregateId = UUID.fromString("9cc42b15-6f27-423e-85f0-4fd24d4846ca")
                        )
                    })


                    events.forEach { event ->
                        run {
                            repository.save(event)
                            event.value?.let { eventPublisher.publishEvent(it) }
                        }
                    }

                    //WHEN

                }
            }
        }
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("9cc42b15-6f27-423e-85f0-4fd24d4846ca")
        var PRODUCT_ID = UUID.fromString("9cc42b15-6f27-423e-85f0-4fd24d4846c1")
    }

}
