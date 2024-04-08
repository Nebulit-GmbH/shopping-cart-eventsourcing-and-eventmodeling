package de.nebulit.inventorychange

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.ProductInventoryAggregateRepository

import de.nebulit.inventorychange.internal.ChangeInventoryCommand;
import de.nebulit.events.InventoryChangedEvent
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import de.nebulit.common.support.RandomData
import de.nebulit.inventorychange.internal.InventoryChangedEventExternal
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario
import java.util.*

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(ContainerConfiguration::class)
class InventoryChangeTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: ProductInventoryAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("32719125-aa10-4eb5-bfba-f6ab60973a70")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `InventoryChangeTest`(scenario: Scenario) {

        //GIVEN


        //WHEN
        var whenResult = scenario.publish(InventoryChangedEventExternal(AGGREGATE_ID, 5))
        //THEN
        whenResult.andWaitForEventOfType(InventoryChangedEvent::class.java)
            .matchingMapped({item -> item.quantity}, {quantity -> quantity == 5})
            .toArrive()
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("32719125-aa10-4eb5-bfba-f6ab60973a70")
    }

}
