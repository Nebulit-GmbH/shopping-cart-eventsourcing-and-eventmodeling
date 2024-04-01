package de.nebulit

import org.springframework.boot.SpringApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName


@TestConfiguration(proxyBeanMethods = false)
internal class ContainerConfiguration {

    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> {
        val postgres = PostgreSQLContainer(DockerImageName.parse("postgres")).withReuse(true)
            .withExposedPorts(5432)
            .withPassword("postgres")
            .withUsername("postgres")
        return postgres
    }

}

object ApplicationStarter {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.from(SpringApp::main)
            .with(ContainerConfiguration::class.java)
            .run(*args)
    }
}
