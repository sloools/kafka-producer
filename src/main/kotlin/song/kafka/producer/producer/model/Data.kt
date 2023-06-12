package song.kafka.producer.producer.model

import io.cloudevents.CloudEventData
import song.kafka.producer.extension.toJsonByteArray

data class Data(
    val accountId: String,
    val phoneNumber: String
) : CloudEventData {
    override fun toBytes(): ByteArray {
        return this.toJsonByteArray()
    }
}
