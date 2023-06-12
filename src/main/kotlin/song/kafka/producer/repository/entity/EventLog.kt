package song.kafka.producer.repository.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@DynamicUpdate
@Entity(name = "event_log")
class EventLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,
    @Column(name = "account_id")
    val accountId: BigInteger?,
    @Column(name = "topic")
    val topic: String? = null,
    @Column(name = "event_id")
    val eventId: String?,
    @Column(name = "source_uri")
    val source: String?,
    @Column(name = "source_package")
    val type: String?,
    @Column(name = "event_subject")
    val subject: String?,
    @Column(name = "spec_version")
    val specVersion: String?,
    @Column(name = "event_created_at")
    val time: LocalDateTime? = now(),
    @Column(name = "event_data")
    val data: String?,
    @Column(name = "is_produced")
    val status: Boolean? = false,
    @Column(name = "re_published_at")
    val scheduledAt: LocalDateTime? = null
) {
    constructor(
        accountId: BigInteger?,
        topic: String?,
        eventId: String?,
        source: String?,
        type: String?,
        subject: String?,
        specVersion: String?,
        time: LocalDateTime?,
        data: String?
    ) : this(
        id = null, accountId, topic, eventId, source, type, subject, specVersion, time, data, status = false, scheduledAt = null
    )

    constructor() : this(
        id = null, accountId = null, topic = null, eventId = null, source = null, type = null, subject = null, specVersion = null, time = null, data = null, status = false, scheduledAt = null
    )
}
