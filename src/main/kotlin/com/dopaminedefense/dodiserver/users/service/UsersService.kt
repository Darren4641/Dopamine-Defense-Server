package com.dopaminedefense.dodiserver.users.service

import com.dopaminedefense.dodiserver.users.dto.*
import com.dopaminedefense.dodiserver.users.entity.Users

interface UsersService {
    fun saveIfEmailNotExists(user: Users) : SignUpRes

    fun signIn(signInReq: SignInReq) : SignInRes

    fun signOut(email: String)

    fun generateProfile(profileReq: ProfileReq) : ProfileRes

    fun getProfile(email: String) : ProfileRes
}