package com.dopaminedefense.dodiserver.alarm.dto

enum class AlarmMessage (
    val title: String,
    val message: String
) {
    KR_INVITE_FRIEND(AlarmType.FRIEND.name, "님이 친구신청을 보냈어요!"),
    EN_INVITE_FRIEND(AlarmType.FRIEND.name, "sent me a friend request!");

    fun format(name: String): String = "$name $message"

}

enum class AlarmType {
    FRIEND,
    MESSAGE,
    SYSTEM
}

class AlarmDto (
    val title: String,
    val content: String,
    val isRead: Boolean,
    val data: String
) {
}

class AlarmBulkDto (
    val friendId: Long,
    val title: String,
    val content: String,
    val data: String?
)
