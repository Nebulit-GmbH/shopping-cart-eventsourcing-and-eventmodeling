package de.nebulit.requestedproductrevocations.internal

import de.nebulit.events.ProductRevocationRequestedEvent
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component
import java.util.*

@Table(name="requestedproductrevocations_product_revocations")
@Entity
data class ProductRevocation(@Id @Column(name = "cart_id") val cartId: UUID, @Column(name= "product_id")val productId: UUID)

interface ProductRevocationRepository : JpaRepository<ProductRevocation, UUID>

@Component
class ProductRevocationPersister(val productRevocationRepository: ProductRevocationRepository) {

    @ApplicationModuleListener
    fun on(revocationRequestedEvent: ProductRevocationRequestedEvent) {
        productRevocationRepository.save(ProductRevocation(revocationRequestedEvent.aggregateId, revocationRequestedEvent.productId))
    }
}
