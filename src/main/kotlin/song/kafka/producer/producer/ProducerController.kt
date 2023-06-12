package song.kafka.producer.producer

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import song.kafka.producer.producer.model.Message

@RestController
class ProducerController(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    val logger = LoggerFactory.getLogger(ProducerController::class.java)

    /***
     *  토픽의 응답을 받는 Synchronous Produce 방식
     */
    @PostMapping("/kafka/produce")
    fun produce(@RequestBody message: Message) {
        logger.info(">>> send", message.message)

        try {
            val future = kafkaTemplate.send("sample_topic", message)

            val record = future.get()
            logger.info(record.recordMetadata.offset().toString())
        } catch (e: Exception) {
            // exception handling
        }
    }

    /***
     * Key를 지정해서 보내는 방식
     */
    @PostMapping("/kafka/produce/{key}")
    fun produceWithKey(@RequestBody message: Message, @PathVariable key: String) {
        logger.info(">>> send", message.message)

        kafkaTemplate.send("sample_topic", key, message)
    }

    @PostMapping("/kafka/dlt/produce")
    fun dltProduce(@RequestBody message: Message, request: HttpServletRequest) {
        logger.info(">>> send", message.message)
        logger.info(request.requestURI) // /kafka/dlt/produce
        logger.info(request.requestURL.toString()) // http://localhost:10011/kafka/dlt/produce

        kafkaTemplate.send("dlt-test-topic", message)
    }
}