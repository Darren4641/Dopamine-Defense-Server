package com.dopaminedefense.dodiserver.users.service.impl

import com.dopaminedefense.dodiserver.alarm.dto.AlarmBulkDto
import com.dopaminedefense.dodiserver.alarm.dto.AlarmMessage
import com.dopaminedefense.dodiserver.alarm.dto.AlarmType
import com.dopaminedefense.dodiserver.alarm.entity.Alarm
import com.dopaminedefense.dodiserver.alarm.repository.AlarmJDBCRepository
import com.dopaminedefense.dodiserver.alarm.repository.AlarmRepository
import com.dopaminedefense.dodiserver.common.exception.DodiException
import com.dopaminedefense.dodiserver.users.dto.*
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.convertLocalToUtcDate
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.getTodayLocalDateTimeStr
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.getTodayUtcDateTimeStr
import com.dopaminedefense.dodiserver.users.entity.Friend
import com.dopaminedefense.dodiserver.users.repository.user.FriendRepository
import com.dopaminedefense.dodiserver.users.repository.user.UsersRepository
import com.dopaminedefense.dodiserver.users.service.FriendService
import com.example.kopring.common.status.ResultCode
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service


@Service
class FriendServiceImpl (
    val usersRepository: UsersRepository,
    val friendRepository: FriendRepository,
    val alarmRepository: AlarmRepository,
    val alarmJDBCRepository: AlarmJDBCRepository
) : FriendService {


    override fun getFriend(email: String): FriendRes {
        val user = usersRepository.findByEmail(email) ?: throw DodiException(ResultCode.NOT_FOUND)

        val friends = usersRepository.getFriendBlockData(email, getTodayUtcDateTimeStr())

        return FriendRes(friends)
    }


    override fun addFriend(addFriend: AddFriendReq): AddFriendRes {
        val user = usersRepository.findByEmail(addFriend.email) ?: throw DodiException(ResultCode.NOT_FOUND)

        //Users 테이블에 있고, 온보딩 진행 여부 확인
        val friend = usersRepository.findByFriendEmail(addFriend.friendEmail) ?: throw DodiException(ResultCode.FRIEND_CONDITION)

        //이미 친구인지 확인
        if(friendRepository.existsByEmailAndTargetUser_Email(addFriend.email, addFriend.friendEmail)) {
            throw DodiException(ResultCode.ALREADY_FRIEND)
        }

        friendRepository.save(Friend(
            status = FriendStatus.INVITED,
            email = addFriend.email,
            targetUser = friend
        ))

        alarmRepository.save(Alarm(
            title = if(CountryCode.valueOf(user.countryCode!!) == CountryCode.KR) AlarmMessage.KR_INVITE_FRIEND.title else AlarmMessage.EN_INVITE_FRIEND.title,
            content = if(CountryCode.valueOf(user.countryCode!!) == CountryCode.KR) AlarmMessage.KR_INVITE_FRIEND.format(user.name!!) else AlarmMessage.EN_INVITE_FRIEND.format(user.name!!),
            isRead = false,
            isSend = true,
            user = friend,
            sender = user.email,
            localDate = getTodayLocalDateTimeStr(CountryCode.valueOf(friend.countryCode!!))
        ))
        return AddFriendRes(FriendStatus.INVITED, addFriend.friendEmail)
    }

    @Transactional
    override fun updateFriendStatus(updateFriend: UpdateFriendReq): UpdateFriendRes {
        val user = usersRepository.findByEmail(updateFriend.email) ?: throw DodiException(ResultCode.NOT_FOUND)

        val inviter = usersRepository.findByEmail(updateFriend.friendEmail) ?: throw DodiException(ResultCode.NOT_FOUND)

        val friendShip = friendRepository.findByEmailAndTargetUser_Email(updateFriend.friendEmail, updateFriend.email) ?: throw DodiException(ResultCode.NOT_FOUND)

        friendShip.updateStatus(updateFriend.status)

        if(updateFriend.status == FriendStatus.ACCEPTED) {
            friendRepository.save(Friend(
                status = FriendStatus.ACCEPTED,
                email = user.email,
                targetUser = inviter
            ))
        }

        return UpdateFriendRes(updateFriend.status, updateFriend.friendEmail)
    }

    override fun sendMessage(sendMessageReq: SendMessageReq): List<AlarmBulkDto> {
        val user = usersRepository.findByEmail(sendMessageReq.email) ?: throw DodiException(ResultCode.NOT_FOUND)

        var alarmBulkDtoMap = emptyMap<Long, AlarmBulkDto>().toMutableMap()

        sendMessageReq.friendId.forEach{ friendId ->
            val friend = friendRepository.findByEmailAndTargetUser_Id(user.email, friendId)
            if(friend != null) {
                alarmBulkDtoMap[friendId] = AlarmBulkDto(
                    friendId = friendId,
                    title = AlarmType.MESSAGE.name,
                    content = sendMessageReq.context,
                    sender = user.email,
                    localDate = getTodayLocalDateTimeStr(CountryCode.valueOf(friend.targetUser.countryCode!!))
                )
            }
        }

        alarmJDBCRepository.alarmBulkInsert(user.id!!, alarmBulkDtoMap)

        return alarmBulkDtoMap.values.toList()
    }


}