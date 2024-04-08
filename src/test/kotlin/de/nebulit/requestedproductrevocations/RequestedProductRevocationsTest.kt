package de.nebulit.requestedproductrevocations

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.DelegatingQueryHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository
import de.nebulit.common.persistence.InternalEvent


import de.nebulit.events.ProductRevocationRequestedEvent;
import de.nebulit.requestedproductrevocations.RequestedProductRevocationsReadModel
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import de.nebulit.common.support.RandomData
import de.nebulit.requestedproductrevocations.internal.ProductRevocation
import org.assertj.core.api.Assertions.assertThat
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario
import java.util.*

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(ContainerConfiguration::class)
class RequestedProductRevocationsTest {

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
    fun `RequestedProductRevocationsTest`(scenario: Scenario) {

        var whenResult = prepare(scenario)

        //THEN
        whenResult.andWaitForStateChange {
            var readModel: RequestedProductRevocationsReadModel =
                queryHandler.handleQuery(RequestedProductRevocationsReadModelQuery())
            assertThat(readModel.productRevocations).contains(ProductRevocation(AGGREGATE_ID, Companion.PRODUCT_ID))
        }
    }

    private fun prepare(scenario: Scenario): Scenario.When<Void> {
        return scenario.stimulate { stimulus, eventPublisher ->
            run {
                stimulus.executeWithoutResult {
                    //GIVEN

                    var events = mutableListOf<InternalEvent>()

                    events.add(RandomData.newInstance(listOf("value")) {
                        aggregateId = UUID.fromString("212d98f4-0f91-4697-8849-c3309b7f3edb")
                        this.value = ProductRevocationRequestedEvent(
                            productId = PRODUCT_ID,
                            aggregateId = UUID.fromString("212d98f4-0f91-4697-8849-c3309b7f3edb")
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
        var AGGREGATE_ID = UUID.fromString("212d98f4-0f91-4697-8849-c3309b7f3edb")
        var PRODUCT_ID = UUID.randomUUID()
    }

}
