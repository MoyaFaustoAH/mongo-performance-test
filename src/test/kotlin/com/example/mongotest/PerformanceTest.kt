package com.example.mongotest

import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.core.MongoTemplate
import java.time.Duration
import java.util.UUID
import kotlin.time.measureTimedValue

@SpringBootTest
@Import(MongoDbTestcontainersConfiguration::class)
class PerformanceTest {
    private val logger = LoggerFactory.getLogger(PerformanceTest::class.java)

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Autowired
    private lateinit var taskService: TaskService

    @BeforeEach
    fun cleanUp() {
        mongoTemplate.db.drop()
    }

    @Test
    @Order(0)
    fun initEnvironment() {
    }

    @Test
    fun `bulkInsert takes less than 1 second to save 50_000 documents`() {
        val duration = taskService.bulkInsert(generateTasks(50_000))
        assertThat(duration).isLessThan(Duration.ofSeconds(1))
    }

    @Test
    fun `saveAll takes more than 15 seconds to save 50_000 documents`() {
        val duration = taskService.saveAll(generateTasks(50_000))
        assertThat(duration).isGreaterThan(Duration.ofSeconds(15))
    }

    private fun generateTasks(numberOfTasks: Int): List<TaskEntity> {
        val faker = Faker()
        logger.info("generateData - Start creating $numberOfTasks tasks.")
        val (entities, duration) =
            measureTimedValue {
                generateSequence { UUID.randomUUID() }
                    .take(numberOfTasks)
                    .map {
                        TaskEntity(
                            it,
                            action = faker.verb().simplePresent(),
                            owner = faker.name().fullName(),
                            product = faker.barcode().gtin12(),
                        )
                    }.toList()
            }
        logger.info("generateData - Finished creating $numberOfTasks tasks. Took $duration")
        return entities
    }
}
