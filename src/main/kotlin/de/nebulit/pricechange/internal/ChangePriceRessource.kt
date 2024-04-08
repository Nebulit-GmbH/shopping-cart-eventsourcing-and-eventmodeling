package de.nebulit.pricechange.internal

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.pricechange.internal.ChangePriceCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.CrossOrigin
import java.util.*


@RestController
class PricechangeRessource(
    private var commandHandler: DelegatingCommandHandler,
    private var applicationEventPublisher: ApplicationEventPublisher
) {

    var logger = KotlinLogging.logger {}


    @CrossOrigin
    @PostMapping("pricechange")
    fun processCommand(
        @RequestParam aggregateId: UUID,
        @RequestParam oldPrice: Double,
        @RequestParam newPrice: Double
    ) {
        commandHandler.handle(ChangePriceCommand(aggregateId, oldPrice, newPrice))
    }

    @PostMapping("pricechangeExternal")
    @Transactional
    fun priceChangeExternal(
        @RequestParam aggregateId: UUID,
        @RequestParam oldPrice: Double,
        @RequestParam newPrice: Double
    ) {
        applicationEventPublisher.publishEvent(PriceChangedEventExternal(aggregateId, oldPrice, newPrice))
    }


}
