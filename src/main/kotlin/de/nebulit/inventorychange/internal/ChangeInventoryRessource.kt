package de.nebulit.inventorychange.internal

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.inventorychange.internal.ChangeInventoryCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.CrossOrigin


@RestController
class InventorychangeRessource(
    private var commandHandler: DelegatingCommandHandler,
    private var applicationEventPublisher: ApplicationEventPublisher
) {

    var logger = KotlinLogging.logger {}


    @CrossOrigin
    @PostMapping("inventorychange")
    fun processCommand(@RequestParam aggregateId: UUID, @RequestParam quantity: Int, @RequestParam productId: UUID) {
        commandHandler.handle(ChangeInventoryCommand(aggregateId, quantity))
    }

    @PostMapping("externalInventoryChange")
    @Transactional
    fun processCommand(@RequestParam aggregateId: UUID, @RequestParam quantity: Int) {
        applicationEventPublisher.publishEvent(InventoryChangedEventExternal(aggregateId, quantity))
    }


}
