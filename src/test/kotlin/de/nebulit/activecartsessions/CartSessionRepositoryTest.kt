package de.nebulit.activecartsessions

import de.nebulit.ContainerConfiguration
import de.nebulit.activecartsessions.internal.ActiveCartSession
import de.nebulit.activecartsessions.internal.CartItem
import de.nebulit.activecartsessions.internal.CartSessionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.modulith.test.ApplicationModuleTest
import java.util.*
import kotlin.jvm.optionals.getOrNull

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(ContainerConfiguration::class)
class CartSessionRepositoryTest {

    @Autowired
    private lateinit var cartSessionRepository: CartSessionRepository

    @Test
    fun persist() {
        val cartSession = ActiveCartSession(UUID.randomUUID())
    cartSessionRepository.save(cartSession)
        assertThat(cartSessionRepository.count()).isEqualTo(1)

        var productId = UUID.randomUUID()
        var cartItem = CartItem(UUID.randomUUID(), productId)
        cartSession.cartItems.add(cartItem)
        cartSessionRepository.save(cartSession)

        assertThat(cartSessionRepository.findByProductId(listOf(productId)).map { it.cartId }).contains(cartSession.cartId)
        assertThat(cartSessionRepository.findById(cartSession.cartId).getOrNull()?.cartItems?.size).isEqualTo(1)
    }
}
