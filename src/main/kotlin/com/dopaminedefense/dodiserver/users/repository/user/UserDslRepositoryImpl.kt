package com.dopaminedefense.dodiserver.users.repository.user


import com.dopaminedefense.dodiserver.alarm.entity.QAlarm.alarm
import com.dopaminedefense.dodiserver.block.dto.Power
import com.dopaminedefense.dodiserver.block.dto.SyncType
import com.dopaminedefense.dodiserver.block.entity.QBlock.block
import com.dopaminedefense.dodiserver.block.entity.QSync.sync
import com.dopaminedefense.dodiserver.users.dto.FriendDto
import com.dopaminedefense.dodiserver.users.dto.FriendStatus
import com.dopaminedefense.dodiserver.users.dto.ProfileRes
import com.dopaminedefense.dodiserver.users.entity.QFriend.friend
import com.dopaminedefense.dodiserver.users.entity.QLog.log
import com.dopaminedefense.dodiserver.users.entity.QUsers.users
import com.dopaminedefense.dodiserver.users.entity.Users
import com.querydsl.core.types.ConstantImpl
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringTemplate
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate
import kotlin.math.max

@Repository
class UserDslRepositoryImpl (
    private val queryFactory: JPAQueryFactory
) : UserDslRepository {

    override fun existByEmail(email: String): Boolean {
        return queryFactory.selectOne()
            .from(users)
            .where(users.email.eq(email))
            .fetchFirst() != null
    }

    override fun findByEmail(email: String) : Users? {
        return queryFactory.select(users)
            .from(users)
            .leftJoin(log).on(log.user().id.eq(users.id)).fetchJoin()
            .leftJoin(friend).on(friend.email.eq(email)).fetchJoin()
            .leftJoin(block).on(block.user().id.eq(users.id)).fetchJoin()
            .leftJoin(sync).on(sync.user().id.eq(users.id)).fetchJoin()
            .leftJoin(alarm).on(alarm.user().id.eq(users.id)).fetchJoin()
            .where(users.email.eq(email))
            .fetchOne()
    }


    override fun findUserProfile(email: String): ProfileRes? {
        return queryFactory.select(
            Projections.constructor(
                ProfileRes::class.java,
                users.email,
                users.name,
                users.countryCode,
                users.interval,
                users.level,
                users.job,
                users.image,
                users.version,
                users.isOnboarding,
                users.shareStatus,
                alarm.count(),
                friend.count(),
                users.profileLink
            ))
        .from(users)
        .leftJoin(alarm).on(alarm.user().id.eq(users.id).and(alarm.isRead.eq(false))).fetchJoin()
        .leftJoin(friend).on(friend.email.eq(email)).fetchJoin()
        .where(users.email.eq(email))
        .groupBy(users.id)
        .fetchOne()
    }

    override fun findByFriendEmail(friendEmail: String) : Users? {
        return queryFactory.select(users)
            .from(users)
            .where(users.email.eq(friendEmail).and(users.isOnboarding.eq(true)))
            .fetchOne()
    }

    override fun getFriendBlockData(email: String, date: String): List<FriendDto> {

        val result = queryFactory.selectDistinct(
            Projections.constructor(
                FriendDto::class.java,
                friend.targetUser().id,
                users.countryCode,
                friend.targetUser().interval,
                friend.targetUser().name,
                friend.targetUser().image,
                friend.targetUser().level,
                block.exceededCount,
                JPAExpressions.selectDistinct(sync.createdDate.max())
                    .from(sync)
                    .where(sync.type.eq(SyncType.DEVICE_ACTIVE)
                        .and(sync.user().email.eq(friend.targetUser().email))
                        .and(sync.pickup.eq(Power.ON))
                        .and(sync.createdDate.contains(date.split(" ")[0])))
                    .orderBy(sync.createdDate.desc()),
                JPAExpressions.selectDistinct(alarm.sendDate.max())
                    .from(alarm)
                    .where(alarm.user().email.eq(friend.targetUser().email)
                        .and(alarm.isSend.eq(true))
                        .and(alarm.sendDate.isNotNull))
                    .orderBy(alarm.sendDate.desc()),
                block.blockCount,
                block.blocks,
                block.pickupOnCount,
                block.restSec,
                friend.targetUser().shareStatus,
            ))
            .from(users)
            .join(friend).on(friend.email.eq(email)
                .and(friend.status.eq(FriendStatus.ACCEPTED))).fetchJoin()
            .leftJoin(block).on(block.user().id.eq(friend.targetUser().id)
                .and(block.createdDate.contains(date.split(" ")[0]))
                .and(block.isSkipped.eq(false))).fetchJoin()
            .fetch()

        return result
    }


}