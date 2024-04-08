package de.nebulit.revokeproductafterpricechange.internal

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.revokeproductafterpricechange.internal.RevokeProductAfterPriceChangeCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging
import org.springframework.web.bind.annotation.CrossOrigin


@RestController
class RevokeproductafterpricechangeRessource(private var commandHandler: DelegatingCommandHandler) {

    var logger = KotlinLogging.logger {}


    @CrossOrigin
    @PostMapping("revokeproductafterpricechange")
    fun processCommand(@RequestParam aggregateId:UUID,@RequestParam productId:UUID) {
        commandHandler.handle(RevokeProductAfterPriceChangeCommand(aggregateId,productId))
    }


}
