package de.nebulit.revokeproductafterpricechange

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository
import de.nebulit.common.persistence.InternalEvent


import de.nebulit.events.CartSessionStartedEvent;
import de.nebulit.events.CarttemAddedEvent;
import de.nebulit.events.ProductRevocationRequestedEvent;
import de.nebulit.events.ProductRevokedEvent
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
class RevokeProductAfterPriceChangeTest {

    @Autowired
    lateinit var repository: EventsEntityRepository
    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler
    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("030f7413-4335-4ff5-8ab5-f754ec823180")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `RevokeProductAfterPriceChangeTest`(scenario: Scenario) {

       var whenResult = scenario.stimulate { stimulus, eventPublisher ->
                run {
                    stimulus.executeWithoutResult {
                        //GIVEN

    var events = mutableListOf<InternalEvent>()

        events.add(RandomData.newInstance(listOf("value")) {
                        	aggregateId = UUID.fromString("030f7413-4335-4ff5-8ab5-f754ec823180")
                        this.value = CartSessionStartedEvent(
                            	aggregateId = UUID.fromString("030f7413-4335-4ff5-8ab5-f754ec823180")
                        )
                    })


        events.add(RandomData.newInstance(listOf("value")) {
                        	aggregateId = UUID.fromString("030f7413-4335-4ff5-8ab5-f754ec823180")
                        this.value = CarttemAddedEvent(
                            	productName = RandomData.newInstance {  },
	price = RandomData.newInstance {  },
	quantity = RandomData.newInstance {  },
	productimage = RandomData.newInstance {  },
	aggregateId = UUID.fromString("030f7413-4335-4ff5-8ab5-f754ec823180"),
	cartItemId = RandomData.newInstance {  },
	productId = UUID.fromString("e3ede415-f60e-4711-bb4d-dbcd6191309a")
                        )
                    })


        events.add(RandomData.newInstance(listOf("value")) {
                        	aggregateId = UUID.fromString("030f7413-4335-4ff5-8ab5-f754ec823180")
                        this.value = ProductRevocationRequestedEvent(
                            	productId = UUID.fromString("e3ede415-f60e-4711-bb4d-dbcd6191309a"),
	aggregateId = UUID.fromString("030f7413-4335-4ff5-8ab5-f754ec823180")
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
                }}

        //THEN
    	whenResult.andWaitForEventOfType(ProductRevokedEvent::class.java)
                        .matching { 	it.productId == UUID.fromString("e3ede415-f60e-4711-bb4d-dbcd6191309a")&&	it.aggregateId == UUID.fromString("030f7413-4335-4ff5-8ab5-f754ec823180") }.toArrive()
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("030f7413-4335-4ff5-8ab5-f754ec823180")
    }

}
