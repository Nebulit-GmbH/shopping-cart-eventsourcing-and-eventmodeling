package de.nebulit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.modulith.Modulith

@Modulith(
        systemName = "System",
        sharedModules =["de.nebulit.support","de.nebulit.common","de.nebulit.domain"],
        useFullyQualifiedModuleNames = true
)
@EnableJpaRepositories
@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = ["de.nebulit","org.springframework.modulith.events.jpa"])
class SpringApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<SpringApp>(*args)
        }

    }
}

