package song.kafka.producer.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import io.cloudevents.kafka.CloudEventSerializer
import java.io.Serializable

@Configuration
@EnableKafka
class KafkaProducerConfig {

    @Value(value = "\${spring.kafka.bootstrap-servers}")
    lateinit var server: String

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(
            DefaultKafkaProducerFactory(
                producerConfigs()
            )
        )
    }

    @Bean
    fun producerConfigs(): Map<String, Serializable> =
        mapOf(
            ProducerConfig.ACKS_CONFIG to "all",
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to server,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to CloudEventSerializer::class.java
        )
}