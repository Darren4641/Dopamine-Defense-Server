package com.dopaminedefense.dodiserver.block.repository

import com.dopaminedefense.dodiserver.block.dto.SyncBulkDto
import com.dopaminedefense.dodiserver.block.dto.SyncDto
import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.convertUtcStrToLocalDateTime
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

@Repository
class SyncJDBCRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    fun syncBulkInsert(userId: Long, syncDtoMap: Map<String, SyncBulkDto>) {
        val query = "INSERT INTO sync " +
                "(interval_time," +
                "pickup," +
                "type," +
                "is_sync," +
                "local_date," +
                "created_date," +
                "updated_date," +
                "user_id)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"

        jdbcTemplate.batchUpdate(query, object : BatchPreparedStatementSetter {
            val syncList = syncDtoMap.values.toList()

            override fun setValues(ps: PreparedStatement, i: Int) {
                val syncData = syncList[i]

                ps.setInt(1, syncData.intervalTime)
                ps.setString(2, syncData.pickup?.name)
                ps.setString(3, syncData.type?.name)
                ps.setBoolean(4, syncData.isSync)
                ps.setString(5, convertUtcStrToLocalDateTime(syncData.utcDateTime, syncData.countryCode))
                ps.setString(6, syncData.utcDateTime)
                ps.setString(7, syncData.utcDateTime)
                ps.setLong(8, userId)
            }

            override fun getBatchSize(): Int {
                return syncDtoMap.size  // Map의 크기를 반환
            }

        })
    }
}