package com.dopaminedefense.dodiserver.users.controller

import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiError
import com.dopaminedefense.dodiserver.common.annotation.SwaggerApiSuccess
import com.dopaminedefense.dodiserver.users.dto.*
import com.dopaminedefense.dodiserver.users.service.UsersService
import com.example.kopring.common.response.BaseResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/user")
@Tag(name = "User API", description = "사용자 관련 API. 회원가입, 로그인, 사용자 정보 조회 등을 포함.")
class UsersController (
    val usersService: UsersService
) {


    @SwaggerApiSuccess(summary = "회원 가입 [SignUpReq, SignUpRes]", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-100]: 이미 회원가입된 계정입니다.
    """)
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid signUpReq: SignUpReq) : BaseResponse<SignUpRes> {
        return BaseResponse(data = usersService.saveIfEmailNotExists(signUpReq.toEntity()))
    }

    @SwaggerApiSuccess(summary = "로그인 [SignInReq, SignInRes]", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    """)
    @PostMapping("/signin")
    fun signIn(@RequestBody @Valid signInReq: SignInReq) : BaseResponse<SignInRes> {
        return BaseResponse(data = usersService.signIn(signInReq))
    }

    @SwaggerApiSuccess(summary = "회원 탈퇴", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    """)
    @DeleteMapping("/signout")
    fun signout(@RequestParam(name = "email") email: String) : BaseResponse<*>{
        usersService.signOut(email)
        return BaseResponse(data = "")
    }

    @SwaggerApiSuccess(summary = "온-보딩 [ProfileReq, ProfileRes]", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    """)
    @PostMapping("/on-boarding")
    fun onboarding(@RequestBody @Valid profileReq: ProfileReq) : BaseResponse<ProfileRes> {
        return BaseResponse(data = usersService.generateProfile(profileReq))
    }

    @SwaggerApiSuccess(summary = "프로필 조회 [ProfileRes]", implementation = BaseResponse::class)
    @SwaggerApiError(description = """
    [D-02]: 일치하는 데이터가 없습니다.
    """)
    @GetMapping("/profile")
    fun myProfile(@RequestParam(name = "email") email: String) : BaseResponse<ProfileRes> {
        return BaseResponse(data = usersService.getProfile(email))
    }


}