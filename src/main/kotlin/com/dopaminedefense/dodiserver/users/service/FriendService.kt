package com.dopaminedefense.dodiserver.users.service

import com.dopaminedefense.dodiserver.alarm.dto.AlarmBulkDto
import com.dopaminedefense.dodiserver.users.dto.*


interface FriendService {

    fun getFriend(email: String) : FriendRes

    fun addFriend(addFriend: AddFriendReq) : AddFriendRes

    fun updateFriendStatus(updateFriend: UpdateFriendReq) : UpdateFriendRes

    fun sendMessage(sendMessageReq: SendMessageReq) : List<AlarmBulkDto>

}