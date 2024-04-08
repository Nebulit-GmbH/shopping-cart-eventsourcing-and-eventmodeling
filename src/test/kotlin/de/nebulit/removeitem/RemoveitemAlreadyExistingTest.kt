package de.nebulit.removeitem

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository

import de.nebulit.removeitem.internal.RemoveItemCommand;

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
class RemoveitemAlreadyExistingTest {

    @Autowired
    lateinit var repository: EventsEntityRepository
    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler
    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("09b1ebbe-0572-4516-8315-f290b20d9f47")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `RemoveitemAlreadyExistingTest`(scenario: Scenario) {

        //GIVEN
    

        //WHEN
    Assertions.assertThrows(CommandException::class.java) {
                          commandHandler.handle(RemoveItemCommand(	aggregateId = UUID.fromString("09b1ebbe-0572-4516-8315-f290b20d9f47"),
	cartItemId = RandomData.newInstance {  }))}

        //THEN
    
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("09b1ebbe-0572-4516-8315-f290b20d9f47")
    }

}
