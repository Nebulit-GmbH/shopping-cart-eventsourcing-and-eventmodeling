package de.nebulit.viewcart

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.DelegatingQueryHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.CartAggregateRepository

import de.nebulit.events.CarttemAddedEvent;
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import de.nebulit.common.support.RandomData
import org.assertj.core.api.Assertions.assertThat
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario
import java.util.*

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(ContainerConfiguration::class)
class ViewCartTest {

    @Autowired
    lateinit var repository: EventsEntityRepository

    @Autowired
    lateinit var queryHandler: DelegatingQueryHandler

    @Autowired
    lateinit var aggregateRepository: CartAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = UUID.fromString("d1bd1e37-a1ac-4e5f-b3f6-46d9e1825332")
            this.events = mutableListOf()
        })
    }

    @Test
    fun ViewCartTest(scenario: Scenario) {

        //GIVEN

        repository.save(RandomData.newInstance(listOf("value")) {
            aggregateId = UUID.fromString("d1bd1e37-a1ac-4e5f-b3f6-46d9e1825332")
            this.value = CarttemAddedEvent(
                productName = RandomData.newInstance { },
                price = 5.0,
                quantity = 2,
                productimage = RandomData.newInstance { },
                aggregateId = UUID.fromString("d1bd1e37-a1ac-4e5f-b3f6-46d9e1825332"),
                cartItemId = RandomData.newInstance { },
                productId = RandomData.newInstance { }
            )
        })


        repository.save(RandomData.newInstance(listOf("value")) {
            aggregateId = UUID.fromString("d1bd1e37-a1ac-4e5f-b3f6-46d9e1825332")
            this.value = CarttemAddedEvent(
                productName = RandomData.newInstance { },
                price = 3.0,
                quantity = 1,
                productimage = RandomData.newInstance { },
                aggregateId = UUID.fromString("d1bd1e37-a1ac-4e5f-b3f6-46d9e1825332"),
                cartItemId = RandomData.newInstance { },
                productId = RandomData.newInstance { }
            )
        })


        //WHEN


        //THEN
        var readModel = queryHandler.handleQuery<UUID,CartItemsReadModel>(CartItemsReadModelQuery(AGGREGATE_ID))
        assertThat(readModel.totalPrice).isEqualTo(13.0)
        assertThat(readModel.cartItems).hasSize(2)
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("d1bd1e37-a1ac-4e5f-b3f6-46d9e1825332")
    }

}
