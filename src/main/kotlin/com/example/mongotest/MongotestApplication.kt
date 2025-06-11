package com.example.mongotest

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.bulkOps
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.UUID
import kotlin.time.measureTime
import kotlin.time.toJavaDuration

@SpringBootApplication
class MongotestApplication

fun main(args: Array<String>) {
    runApplication<MongotestApplication>(*args)
}

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val mongoTemplate: MongoTemplate,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun saveAll(tasks: List<TaskEntity>): Duration {
        logger.info("saveAll - Start saving tasks")
        val duration =
            measureTime {
                taskRepository.saveAll(tasks)
            }
        logger.info("saveAll - Finished saving ${tasks.size} tasks. Took $duration")
        return duration.toJavaDuration()
    }

    fun bulkInsert(tasks: List<TaskEntity>): Duration {
        logger.info("bulkInsert - Start saving tasks")
        val duration =
            measureTime {
                val bulkOps = mongoTemplate.bulkOps<TaskEntity>(BulkOperations.BulkMode.UNORDERED)
                bulkOps.insert(tasks)
                bulkOps.execute()
            }
        logger.info("bulkInsert - Finished saving ${tasks.size} tasks. Took $duration")
        return duration.toJavaDuration()
    }
}

@Repository
interface TaskRepository : MongoRepository<TaskEntity, UUID>

@Document("tasks")
data class TaskEntity(
    @Id
    val id: UUID,
    val action: String,
    val owner: String,
    val product: Long,
)
