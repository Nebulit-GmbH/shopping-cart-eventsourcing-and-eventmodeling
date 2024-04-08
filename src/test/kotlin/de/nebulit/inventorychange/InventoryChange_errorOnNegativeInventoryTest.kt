package de.nebulit.inventorychange

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.ProductInventoryAggregateRepository

import de.nebulit.inventorychange.internal.ChangeInventoryCommand;

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
class InventoryChange_errorOnNegativeInventoryTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: ProductInventoryAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("a966c864-f7c6-48fe-8102-ac582d476a96")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `InventoryChange_errorOnNegativeInventoryTest`(scenario: Scenario) {

        //GIVEN


        //WHEN
        Assertions.assertThrows(CommandException::class.java) {
            commandHandler.handle(
                ChangeInventoryCommand(
                    aggregateId = UUID.fromString("a966c864-f7c6-48fe-8102-ac582d476a96"),
                    quantity = -1
                )
            )
        }

        //THEN

    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("a966c864-f7c6-48fe-8102-ac582d476a96")
    }

}
