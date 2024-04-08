package de.nebulit.pricechange

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.ProductPricingAggregateRepository

import de.nebulit.pricechange.internal.ChangePriceCommand;


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
class PriceChange_errorOnNegativePriceTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: ProductPricingAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("075f805f-1109-4556-9736-bf70fd1a28a3")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `PriceChange_errorOnNegativePriceTest`(scenario: Scenario) {

        //GIVEN


        //WHEN
        Assertions.assertThrows(CommandException::class.java) {
            commandHandler.handle(
                ChangePriceCommand(
                    aggregateId = UUID.fromString("075f805f-1109-4556-9736-bf70fd1a28a3"),
                    oldPrice = RandomData.newInstance { },
                    newPrice = -1.0
                )
            )
        }

        //THEN

    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("075f805f-1109-4556-9736-bf70fd1a28a3")
    }

}
