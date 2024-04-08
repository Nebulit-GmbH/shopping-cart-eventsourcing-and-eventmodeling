package de.nebulit.revokeproductafterpricechange

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository
import de.nebulit.common.persistence.InternalEvent


import de.nebulit.revokeproductafterpricechange.internal.RevokeProductAfterPriceChangeCommand;

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
class RevokeProductAfterPriceChange_noProductRevocationTest {

    @Autowired
    lateinit var repository: EventsEntityRepository
    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler
    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("15964f1b-7ef0-45c2-9d8a-1a070c795470")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `RevokeProductAfterPriceChange_noProductRevocationTest`(scenario: Scenario) {

       var whenResult = scenario.stimulate { stimulus, eventPublisher ->
                run {
                    stimulus.executeWithoutResult {
                        //GIVEN
                        
    var events = mutableListOf<InternalEvent>()
     
     
      events.forEach { event ->
                        run {
                            repository.save(event)
                            event.value?.let { eventPublisher.publishEvent(it) }
                        }
                    }
    

                        //WHEN
                        Assertions.assertThrows(CommandException::class.java) {
                          commandHandler.handle(RevokeProductAfterPriceChangeCommand(	aggregateId = UUID.fromString("15964f1b-7ef0-45c2-9d8a-1a070c795470"),
	productId = RandomData.newInstance {  }))}
                    }
                }}

        //THEN
    
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("15964f1b-7ef0-45c2-9d8a-1a070c795470")
    }

}
