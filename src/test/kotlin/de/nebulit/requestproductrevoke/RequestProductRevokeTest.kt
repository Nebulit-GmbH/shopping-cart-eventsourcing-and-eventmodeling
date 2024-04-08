package de.nebulit.requestproductrevoke

import de.nebulit.ContainerConfiguration

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.support.CartAggregateRepository

import de.nebulit.events.CartSessionStartedEvent;
import de.nebulit.events.CarttemAddedEvent;
import de.nebulit.events.PriceChangedEvent;
import de.nebulit.requestproductrevoke.internal.RequestProductRevocationAfterPriceChangeCommand;
import de.nebulit.events.ProductRevocationRequestedEvent
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import de.nebulit.common.support.RandomData
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario

@ApplicationModuleTest(
    mode = ApplicationModuleTest.BootstrapMode.ALL_DEPENDENCIES,
    extraIncludes = ["de.nebulit.activecartsessions"]
)
@Import(ContainerConfiguration::class)
class RequestProductRevokeTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository


    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("049715f4-7508-4fa7-8e39-7d271cd29f8c")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `RequestProductRevokeTest`(scenario: Scenario) {

        var productId = RandomData.newInstance<UUID> {  }
        //GIVEN
        var whenResult = scenario.stimulate { stimulus, eventPublisher ->
            run {
                stimulus.executeWithoutResult {
                    var events = mutableListOf<InternalEvent>()
                    events.add(RandomData.newInstance<InternalEvent>(listOf("value")) {
                        this.aggregateId = UUID.fromString("049715f4-7508-4fa7-8e39-7d271cd29f8c")
                        this.value = CartSessionStartedEvent(
                            aggregateId = UUID.fromString("049715f4-7508-4fa7-8e39-7d271cd29f8c")
                        )
                    })


                    events.add(RandomData.newInstance<InternalEvent>(listOf("value")) {
                        aggregateId = UUID.fromString("049715f4-7508-4fa7-8e39-7d271cd29f8c")
                        this.value = CarttemAddedEvent(
                            productName = RandomData.newInstance { },
                            price = RandomData.newInstance { },
                            quantity = RandomData.newInstance { },
                            productimage = RandomData.newInstance { },
                            aggregateId = UUID.fromString("049715f4-7508-4fa7-8e39-7d271cd29f8c"),
                            cartItemId = RandomData.newInstance { },
                            productId = productId
                        )
                    })


                    events.add(RandomData.newInstance<InternalEvent>(listOf("value")) {
                        aggregateId = productId
                        this.value = PriceChangedEvent(
                            oldPrice = RandomData.newInstance { },
                            newPrice = RandomData.newInstance { },
                            aggregateId = productId
                        )
                    })

                    events.forEach { event ->
                        run {
                            repository.save(event)
                            event.value?.let { eventPublisher.publishEvent(it) }
                        }
                    }

                    commandHandler.handle(
                        RequestProductRevocationAfterPriceChangeCommand(aggregateId = UUID.fromString("049715f4-7508-4fa7-8e39-7d271cd29f8c"),
                            productId = RandomData.newInstance { })
                    )
                }
            }}


        //THEN
        whenResult.andWaitForEventOfType(ProductRevocationRequestedEvent::class.java)
            .toArrive()
    }


    companion object {
        var AGGREGATE_ID = UUID.fromString("049715f4-7508-4fa7-8e39-7d271cd29f8c")
    }

}
