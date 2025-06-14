package com.example.mongotest

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MongoDBContainer

@TestConfiguration(proxyBeanMethods = false)
class MongoDbTestcontainersConfiguration {
    @Bean
    @ServiceConnection
    fun mongoConnection() = MongoDBContainer("mongo:8.0.10-noble")
}
