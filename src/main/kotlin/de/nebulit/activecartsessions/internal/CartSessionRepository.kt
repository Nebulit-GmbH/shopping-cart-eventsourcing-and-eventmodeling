package de.nebulit.activecartsessions.internal

import jakarta.persistence.*
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

@Embeddable
data class CartItem(@Column(name="cart_item_id") var cartItemId: UUID,
                    @Column(name="product_id")  var productId: UUID)

@Entity
@Table(name = "cart_sessions")
class ActiveCartSession(@Id @Column(name="cart_id") var cartId: UUID) {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cart_session_items", joinColumns = [JoinColumn(name = "cart_id")])
    var cartItems = mutableListOf<CartItem>()
}

interface CartSessionRepository : JpaRepository<ActiveCartSession, UUID> {

    @Query("SELECT c FROM ActiveCartSession c JOIN c.cartItems ci WHERE ci.productId in :productIds")
    fun findByProductId(@Param("productIds") productIds: List<UUID>): List<ActiveCartSession>
}
