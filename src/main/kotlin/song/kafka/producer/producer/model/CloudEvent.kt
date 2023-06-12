package song.kafka.producer.producer.model

import com.fasterxml.jackson.annotation.JsonInclude
import io.cloudevents.CloudEvent
import io.cloudevents.CloudEventData
import io.cloudevents.SpecVersion
import java.net.URI
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class CloudEvent(
    private val id: String,
    private val source: URI,
    private val type: String, // package name
    private val subject: String,
    private val specVersion: SpecVersion,
    private val time: OffsetDateTime,
    private val data: CloudEventData?
) : CloudEvent {

    constructor(
        source: String,
        type: String,
        subject: String,
        data: CloudEventData? = null
    ) : this(
        id = UUID.randomUUID().toString(),
        source = URI(source),
        type = type,
        subject = subject,
        specVersion = SpecVersion.V1,
        time = OffsetDateTime.of(now(), ZoneOffset.UTC),
        data = data
    )
    override fun getSpecVersion(): SpecVersion {
        return specVersion
    }

    override fun getId(): String {
        return id
    }

    override fun getType(): String {
        return type
    }

    override fun getSource(): URI {
        return source
    }

    override fun getDataContentType(): String? {
        return null
    }

    override fun getDataSchema(): URI? {
        return null
    }

    override fun getSubject(): String? {
        return subject
    }

    override fun getTime(): OffsetDateTime? {
        return time
    }

    override fun getData(): CloudEventData? {
        return data
    }

    override fun getAttribute(attributeName: String): Any? {
        return null
    }

    override fun getExtension(extensionName: String): Any? {
        return null
    }

    override fun getExtensionNames(): MutableSet<String> {
        return mutableSetOf()
    }

    override fun toString(): String {
        return this.toString()
    }
}