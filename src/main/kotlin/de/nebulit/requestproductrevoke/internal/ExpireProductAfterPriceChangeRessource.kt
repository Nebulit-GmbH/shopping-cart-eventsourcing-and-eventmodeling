package de.nebulit.requestproductrevoke.internal

import de.nebulit.common.DelegatingCommandHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging
import org.springframework.web.bind.annotation.CrossOrigin


@RestController
class RequestproductrevokeRessource(private var commandHandler: DelegatingCommandHandler) {

    var logger = KotlinLogging.logger {}


    @CrossOrigin
    @PostMapping("requestproductrevoke")
    fun processCommand(@RequestParam aggregateId:UUID,@RequestParam productId:UUID) {
        commandHandler.handle(RequestProductRevocationAfterPriceChangeCommand(aggregateId,productId))
    }


}
