package de.nebulit.pricechange

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.ProductPricingAggregateRepository

import de.nebulit.pricechange.internal.ChangePriceCommand;
import de.nebulit.events.PriceChangedEvent

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
class PriceChangeTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: ProductPricingAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("81abd426-e484-48f6-9422-2485d370ec0c")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `PriceChangeTest`(scenario: Scenario) {

        //GIVEN


        //WHEN
        var whenResult = scenario.stimulate { _ ->
            commandHandler.handle(
                ChangePriceCommand(aggregateId = UUID.fromString("81abd426-e484-48f6-9422-2485d370ec0c"),
                    oldPrice = RandomData.newInstance { },
                    newPrice = RandomData.newInstance { })
            )
        }

        //THEN
        whenResult.andWaitForEventOfType(PriceChangedEvent::class.java)
            .toArrive()
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("81abd426-e484-48f6-9422-2485d370ec0c")
    }

}
