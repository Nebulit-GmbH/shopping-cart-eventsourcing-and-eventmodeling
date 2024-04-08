package de.nebulit.clearcart.internal

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.clearcart.internal.ClearCartCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging
import org.springframework.web.bind.annotation.CrossOrigin


@RestController
class ClearcartRessource(private var commandHandler: DelegatingCommandHandler) {

    var logger = KotlinLogging.logger {}


    @CrossOrigin
    @PostMapping("clearcart")
    fun processCommand(@RequestParam aggregateId:UUID) {
        commandHandler.handle(ClearCartCommand(aggregateId))
    }


}
