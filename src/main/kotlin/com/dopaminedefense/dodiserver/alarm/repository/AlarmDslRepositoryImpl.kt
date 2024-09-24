package com.dopaminedefense.dodiserver.alarm.repository

import com.dopaminedefense.dodiserver.alarm.dto.AlarmDto
import com.dopaminedefense.dodiserver.alarm.entity.QAlarm.alarm
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.getTodayUtcDateTimeStr
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory


open class AlarmDslRepositoryImpl (
    private val queryFactory: JPAQueryFactory
) : AlarmDslRepository {

    override fun getAlarms(email: String) : List<AlarmDto> {
        val result = queryFactory.select(
            Projections.constructor(
                AlarmDto::class.java,
                alarm.title,
                alarm.content,
                alarm.isRead,
                alarm.data
            ))
            .from(alarm)
            .where(alarm.user().email.eq(email).and(alarm.isSend.eq(true)))
            .orderBy(alarm.createdDate.desc())
            .fetch()

        queryFactory.update(alarm)
            .set(alarm.isRead, true)
            .where(alarm.user().email.eq(email).and(alarm.isSend.eq(true)))
            .execute()

        return result
    }

    override fun getAlarmsForNotification(email: String) : List<AlarmDto> {
        val result = queryFactory.select(
            Projections.constructor(
                AlarmDto::class.java,
                alarm.title,
                alarm.content,
                alarm.isRead,
                alarm.data
            ))
            .from(alarm)
            .where(alarm.user().email.eq(email).and(alarm.isSend.eq(false)))
            .orderBy(alarm.createdDate.desc())
            .fetch()

        queryFactory.update(alarm)
            .set(alarm.sendDate, getTodayUtcDateTimeStr())
            .set(alarm.isSend, true)
            .where(alarm.user().email.eq(email).and(alarm.isSend.eq(false)))
            .execute()

        return result
    }
}