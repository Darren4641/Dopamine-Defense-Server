package com.dopaminedefense.dodiserver.users.service.impl

import com.dopaminedefense.dodiserver.alarm.service.SlackService
import com.dopaminedefense.dodiserver.common.exception.DodiException
import com.dopaminedefense.dodiserver.users.dto.*
import com.dopaminedefense.dodiserver.users.entity.Users
import com.dopaminedefense.dodiserver.users.repository.user.UsersRepository
import com.dopaminedefense.dodiserver.users.service.UsersService
import com.example.kopring.common.status.ResultCode
import com.google.gson.Gson
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UsersServiceImpl (
    val slackService: SlackService,
    val userRepository: UsersRepository
) : UsersService {

    val gson: Gson = Gson()

    @Transactional
    override fun saveIfEmailNotExists(user: Users) : SignUpRes {
        return if(userRepository.existByEmail(user.email)) {
            throw DodiException(ResultCode.MEMBER_ALREADY)
        } else {
            val signUpUser = userRepository.save(user)
            slackService.sendSlackMessage(signUpUser.email + "[" + signUpUser.name + "님이 Dodi에 회원가입하였습니다. [누적 가입자 수]" + userRepository.countByStatus(UserStatus.ACTIVE) + "명")
             SignUpRes(
                 id = signUpUser.id!!,
                 email = signUpUser.email,
                 utcDateTime = signUpUser.createdDate
            )
        }
    }

    @Transactional
    override fun signIn(signInReq: SignInReq): SignInRes {
        return userRepository.findByEmail(signInReq.email)?.let {
            it.signIn()
            val result = SignInRes(
                id = it.id!!,
                email = it.email,
                countryZone = CountryCode.valueOf(it.countryCode!!).zone,
                latestLoginDateTime = it.latestLoginTime!!,
                utcDateTime = it.createdDate
            )
            result
        } ?: throw DodiException(ResultCode.NOT_FOUND)
    }

    override fun signOut(email: String) {
        val user = userRepository.findByEmail(email) ?: throw DodiException(ResultCode.NOT_FOUND)
        user.signOut()
        userRepository.save(user)
    }

    override fun generateProfile(profileReq: ProfileReq): ProfileRes {
        val user = userRepository.findByEmail(profileReq.email) ?: throw DodiException(ResultCode.NOT_FOUND)
        val shareStatusStr = gson.toJson(profileReq.shareStatus)
        val userProfile = userRepository.save(user.onBoarding(profileReq, shareStatusStr))

        return ProfileRes(
            email = user.email,
            name = userProfile.name!!,
            countryZone = CountryCode.valueOf(user.countryCode!!).zone,
            intervalTime = userProfile.intervalTime?.let { Interval.fromValue(it) }?: Interval.M15,
            level = userProfile.level?.let { Level.fromValue(it) }?: Level.EASY,
            job = userProfile.job,
            image = userProfile.image,
            version = userProfile.version,
            isOnboarding = userProfile.isOnboarding,
            shareStatus = profileReq.shareStatus,
            alarmCount = 0
        )
    }

    override fun getProfile(email: String): ProfileRes {
        val user = userRepository.findByEmail(email) ?: throw DodiException(ResultCode.NOT_FOUND)

        if(!user.isOnboarding) {
            throw DodiException(ResultCode.ON_BOARDING)
        }
        val userProfile = userRepository.findUserProfile(email) ?: throw DodiException(ResultCode.NOT_FOUND)

        return userProfile

    }

}