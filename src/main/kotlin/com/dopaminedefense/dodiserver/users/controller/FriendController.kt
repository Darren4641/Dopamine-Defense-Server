package com.dopaminedefense.dodiserver.users.controller

import com.dopaminedefense.dodiserver.alarm.dto.AlarmBulkDto
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiError
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiSuccess
import com.dopaminedefense.dodiserver.users.dto.*
import com.dopaminedefense.dodiserver.users.service.FriendService
import com.example.kopring.common.response.BaseResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/friend")
@Tag(name = "Friend API", description = "친구 관련 API. 친구 추가, 친구 목록, 친구 수락/거절, 메시지 보내기 등을 포함.")
class FriendController (
   val friendService: FriendService
) {


    @SwaggerApiSuccess(summary = "친구 목록 [FriendRes]", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    """)
    @GetMapping("")
    fun getFriend(@RequestParam(name = "email") email: String) : BaseResponse<FriendRes> {
        return BaseResponse(data = friendService.getFriend(email))
    }

    @SwaggerApiSuccess(summary = "친구 추가 [AddFriendReq, AddFriendRes]", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    [D-05]: 존재하지 않은 이메일 혹은 온보딩이 진행되지 않았습니다.
    [D-06]: 이미 친구 추가된 사용자입니다.
    """)
    @PostMapping("")
    fun addFriend(@RequestBody @Valid addFriend: AddFriendReq) : BaseResponse<AddFriendRes> {
        return BaseResponse(data = friendService.addFriend(addFriend))
    }

    @SwaggerApiSuccess(summary = "친구 수락/거절 [UpdateFriendReq, UpdateFriendRes]", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    """)
    @PatchMapping("")
    fun updateFriendStatus(@RequestBody @Valid updateFriend: UpdateFriendReq) : BaseResponse<UpdateFriendRes> {
        return BaseResponse(data = friendService.updateFriendStatus(updateFriend))
    }

    @SwaggerApiSuccess(summary = "친구에게 메시지 전송 [SendMessageReq, List<AlarmBulkDto>]", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    """)
    @PostMapping("/message")
    fun sendMessageToFriend(@RequestBody @Valid sendMessageReq: SendMessageReq) : BaseResponse<List<AlarmBulkDto>> {
        return BaseResponse(data = friendService.sendMessage(sendMessageReq))
    }
}