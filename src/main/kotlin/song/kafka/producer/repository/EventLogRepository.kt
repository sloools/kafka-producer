package song.kafka.producer.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import song.kafka.producer.repository.entity.EventLog

@Repository
interface EventLogRepository : JpaRepository<EventLog, Int>