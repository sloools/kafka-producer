package song.kafka.producer.extension

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JSONHelper {
    companion object {
        val ISO_8061_TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        private val objectMapper = object : ThreadLocal<ObjectMapper>() {
            override fun initialValue(): ObjectMapper {
                val objectMapper = jacksonObjectMapper()

                val formatter = DateTimeFormatter.ofPattern(ISO_8061_TIMESTAMP)
                val localDateTimeSerializer = LocalDateTimeSerializer(formatter)
                val localDateTimeDeserializer = LocalDateTimeDeserializer(formatter)

                val javaTimeModule = JavaTimeModule()
                javaTimeModule.addSerializer(LocalDateTime::class.java, localDateTimeSerializer)
                javaTimeModule.addDeserializer(LocalDateTime::class.java, localDateTimeDeserializer)

                objectMapper.registerModule(javaTimeModule)

                return objectMapper
            }
        }

        fun getObjectMapper(): ObjectMapper = objectMapper.get()

        fun toJson(value: Any, isPrettyPrint: Boolean = false): String {
            return if (isPrettyPrint) getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(value)
            else getObjectMapper().writeValueAsString(value)
        }

        fun toJsonByteArray(value: Any): ByteArray {
            return getObjectMapper().writeValueAsBytes(value)
        }

        inline fun <reified T : Any> fromJson(content: String): T {
            return getObjectMapper().readValue(content)
        }

        inline fun <reified T : Any> fromJson(content: ByteArray): T {
            return getObjectMapper().readValue(content)
        }
    }
}

fun Any.toJson(isPrettyPrint: Boolean = false): String {
    return JSONHelper.toJson(this, isPrettyPrint)
}

fun Any.toJsonByteArray(): ByteArray {
    return JSONHelper.toJsonByteArray(this)
}

inline fun <reified T : Any> String.toObject(): T {
    return JSONHelper.fromJson(this)
}

inline fun <reified T : Any> ByteArray.toObject(): T {
    return JSONHelper.fromJson(this)
}

fun <T> String.toObject(valueType: Class<T>): T {
    return JSONHelper.getObjectMapper().readValue(this, valueType)
}

fun String.getValue(key: String): Any? {
    val a = JSONHelper.getObjectMapper().readValue(this, HashMap::class.java)
    return a[key]
}

fun String.addNode(key: String, value: String): String {
    return JSONHelper.getObjectMapper().readTree(this)?.let { node ->
        (node as ObjectNode).put(key, value).toString()
    } ?: this
}

fun String.deleteNode(key: String): String {
    return try {
        JSONHelper.getObjectMapper().readTree(this)?.let { node ->
            (node as ObjectNode).remove(key).toString()
            node.toString()
        } ?: this
    } catch (e: NullPointerException) {
        this
    }
}