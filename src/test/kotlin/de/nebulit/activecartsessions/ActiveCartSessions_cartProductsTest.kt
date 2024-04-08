package de.nebulit.activecartsessions

import de.nebulit.ContainerConfiguration
import de.nebulit.activecartsessions.internal.ActiveCartSession
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository

import de.nebulit.events.CarttemAddedEvent;
import de.nebulit.activecartsessions.internal.CartProductsReadModel
import de.nebulit.activecartsessions.internal.CartProductsReadModelQuery
import de.nebulit.activecartsessions.internal.CartSessionRepository
import de.nebulit.common.DelegatingQueryHandler
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import de.nebulit.common.support.RandomData
import de.nebulit.events.CartSessionStartedEvent
import org.assertj.core.api.Assertions.assertThat
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario
import java.util.*

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(ContainerConfiguration::class)
class ActiveCartSessions_cartProductsTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler

    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @Autowired
    lateinit var cartSessionRepository: CartSessionRepository

    @Autowired
    lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Autowired
    lateinit var delegatingQueryHandler: DelegatingQueryHandler

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("62c4c27b-c9f2-4278-b69c-b156426bb5d6")
            this.events = mutableListOf()
        })
    }

    @Test
    fun `ActiveCartSessions_cartProductsTest`(scenario: Scenario) {

        //GIVEN
        var productId = RandomData.newInstance<UUID> { }
        cartSessionRepository.save(ActiveCartSession(AGGREGATE_ID))

        var whenResult = scenario.stimulate(Runnable {

            applicationEventPublisher.publishEvent(
                CarttemAddedEvent(
                    productName = RandomData.newInstance { },
                    price = RandomData.newInstance { },
                    quantity = RandomData.newInstance { },
                    productimage = RandomData.newInstance { },
                    aggregateId = AGGREGATE_ID,
                    cartItemId = RandomData.newInstance { },
                    productId = productId
                )
            )

        })

        whenResult.andWaitForStateChange {
            var readModel:CartProductsReadModel =
                delegatingQueryHandler.handleQuery(CartProductsReadModelQuery(listOf(productId)))
            readModel.data.contains(AGGREGATE_ID)
        }

        //THEN

    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("62c4c27b-c9f2-4278-b69c-b156426bb5d6")
    }

}
