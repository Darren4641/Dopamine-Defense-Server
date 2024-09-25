package com.dopaminedefense.dodiserver.alarm.dto

import io.swagger.v3.oas.annotations.media.Schema

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
    @Schema(description = "알림 제목 MESSAGE(친구끼리 메시지), FRIEND(친구 추가 알림)", example = "MESSAGE")
    val title: String,
    @Schema(description = "알림 내용", example = "이 알림은 받는 친구의 Exceed 시간에 전송됩니다~")
    val content: String,
    @Schema(description = "isSend가 true인거만 한해서 읽음 여부 (알림 탭 누르면 true됨)", example = "true")
    val isRead: Boolean,
    @Schema(description = "추가적으로 필요한 파라미터 담는 곳", example = "Darren")
    val data: String
) {
}

class AlarmBulkDto (
    @Schema(description = "친구 Auto ID", example = "2")
    val friendId: Long,
    @Schema(description = "알림 제목 MESSAGE(친구끼리 메시지), FRIEND(친구 추가 알림)", example = "MESSAGE")
    val title: String,
    @Schema(description = "알림 내용", example = "이 알림은 받는 친구의 Exceed 시간에 전송됩니다~")
    val content: String,
    @Schema(description = "보낸 사람", example = "zxz4641@gmail.com")
    val sender: String?,
    @Schema(description = "사용자 Local Date", example = "2024-09-09 12:00:00")
    val localDate: String
)
