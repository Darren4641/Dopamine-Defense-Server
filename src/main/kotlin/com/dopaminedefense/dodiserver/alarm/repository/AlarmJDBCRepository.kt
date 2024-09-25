package com.dopaminedefense.dodiserver.alarm.repository

import com.dopaminedefense.dodiserver.alarm.dto.AlarmBulkDto
import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.getTodayUtcDateTimeStr
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId

@Repository
class AlarmJDBCRepository (
    private val jdbcTemplate: JdbcTemplate
) {

    fun alarmBulkInsert(userId: Long, alarmDtoMap: Map<Long, AlarmBulkDto>) {
        val query = "INSERT INTO alarm " +
                "(title," +
                "content," +
                "is_read," +
                "is_send," +
                "sender," +
                "local_date," +
                "created_date," +
                "updated_date," +
                "user_id)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"

        jdbcTemplate.batchUpdate(query, object : BatchPreparedStatementSetter {
            val alarmList = alarmDtoMap.values.toList()

            override fun setValues(ps: PreparedStatement, i: Int) {
                val alarmData = alarmList[i]

                ps.setString(1, alarmData.title)
                ps.setString(2, alarmData.content)
                ps.setBoolean(3, false)
                ps.setBoolean(4, false)
                ps.setString(5, alarmData.sender)
                ps.setString(6, alarmData.localDate)
                ps.setString(7, getTodayUtcDateTimeStr())
                ps.setString(8, getTodayUtcDateTimeStr())
                ps.setLong(9, alarmData.friendId)
            }

            override fun getBatchSize(): Int {
                return alarmDtoMap.size  // Map의 크기를 반환
            }

        })
    }
}