package song.kafka.producer.producer

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import song.kafka.producer.extension.toJson
import song.kafka.producer.producer.model.CloudEvent
import song.kafka.producer.producer.model.Data
import song.kafka.producer.producer.model.Message
import song.kafka.producer.repository.EventLogRepository
import song.kafka.producer.repository.entity.EventLog
import java.math.BigInteger

@RestController
class AsyncProducerController(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val eventLogRepository: EventLogRepository
) {
    val logger = LoggerFactory.getLogger(AsyncProducerController::class.java)

    /***
     *  토픽의 응답을 받는 Asynchronous Produce 방식
     */
    @PostMapping("/kafka/async/produce")
    fun produce(@RequestBody message: Message) {
        logger.info(">>> send", message.message)
        logger.info(Thread.currentThread().toString())

        val future = kafkaTemplate.send("sample_topic", message)

        future.thenAccept {
            logger.info("카프카의 producer default 방식은 비동기 !")
        }
    }

    /***
     * Event DB와 도메인 행위를 한 트랜잭션으로 묶음
     */
    @PostMapping("/kafka/event-db/produce/{accountId}")
    fun produceWithEventDb(@PathVariable accountId: String, request: HttpServletRequest) {
        logger.info(">>> send", accountId)

        val event = CloudEvent(
            source = request.requestURI,
            type = this.javaClass.packageName,
            subject = "sample",
            data = Data(accountId = accountId, phoneNumber = "01000000000")
        )

        val eventLog = EventLog(
            accountId = BigInteger.ONE,
            topic = "scheduler_test",
            eventId = event.id,
            source = event.source.toString(),
            type = event.type,
            subject = event.subject,
            specVersion = event.specVersion.toString(),
            time = event.time?.toLocalDateTime(),
            data = event.data?.toJson()
        )
//        println("evene Id = ${event.id}")
        // Domain Transaction

        // Save Event DB
        eventLogRepository.save(eventLog)

        val future = kafkaTemplate.send("scheduler_test", event)

        future.thenAccept {
            logger.info("Scheduler Test !")
        }
    }
}