package song.kafka.producer.producer.model

import java.time.LocalDateTime

data class Message(
    val name: String?,
    val message: String?,
    var timestamp: String? = LocalDateTime.now().toString()
)
