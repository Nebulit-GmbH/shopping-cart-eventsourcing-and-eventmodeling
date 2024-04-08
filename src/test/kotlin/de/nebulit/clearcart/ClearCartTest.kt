package de.nebulit.clearcart

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository

import de.nebulit.clearcart.internal.ClearCartCommand;
import de.nebulit.events.CartClearedEvent
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
class ClearCartTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("67e5ab1c-89cf-4355-b74b-14eb4fda393c")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `ClearCartTest`(scenario: Scenario) {

        //GIVEN


        //WHEN
        var whenResult = scenario.stimulate { _ ->
            commandHandler.handle(ClearCartCommand(aggregateId = UUID.fromString("67e5ab1c-89cf-4355-b74b-14eb4fda393c")))
        }

        //THEN
        whenResult.andWaitForEventOfType(CartClearedEvent::class.java)
            .toArrive()
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("67e5ab1c-89cf-4355-b74b-14eb4fda393c")
    }

}
