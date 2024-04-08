package de.nebulit.additem.internal

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.additem.internal.AddItemCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging
import org.springframework.web.bind.annotation.CrossOrigin


@RestController
class AdditemRessource(private var commandHandler: DelegatingCommandHandler) {

    var logger = KotlinLogging.logger {}


    @CrossOrigin
    @PostMapping("additem")
    fun processCommand(@RequestParam productName:String,@RequestParam price:Double,@RequestParam aggregateId:UUID,@RequestParam quantity:Int,@RequestParam productimage:String,@RequestParam productId:UUID) {
        commandHandler.handle(AddItemCommand(productName,price,aggregateId,quantity,productimage,productId))
    }


}
