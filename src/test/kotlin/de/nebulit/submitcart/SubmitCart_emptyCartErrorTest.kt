package de.nebulit.submitcart

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository

import de.nebulit.submitcart.internal.SubmitCartCommand;

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
class SubmitCart_emptyCartErrorTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("c2f9c235-9a07-451c-bab4-1991a5c23c84")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `SubmitCart_emptyCartErrorTest`(scenario: Scenario) {

        //GIVEN


        //WHEN
        Assertions.assertThrows(CommandException::class.java) {
            commandHandler.handle(SubmitCartCommand(aggregateId = UUID.fromString("c2f9c235-9a07-451c-bab4-1991a5c23c84")))
        }

        //THEN

    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("c2f9c235-9a07-451c-bab4-1991a5c23c84")
    }

}
