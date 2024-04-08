package de.nebulit.submitcart.internal

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.submitcart.internal.SubmitCartCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging


@RestController
class SubmitcartRessource(private var commandHandler: DelegatingCommandHandler) {

    var logger = KotlinLogging.logger {}


    @PostMapping("submitcart")
    fun processCommand(@RequestParam aggregateId:UUID) {
        commandHandler.handle(SubmitCartCommand(aggregateId))
    }


}
