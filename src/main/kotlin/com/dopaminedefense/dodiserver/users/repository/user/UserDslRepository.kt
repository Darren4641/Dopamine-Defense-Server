package com.dopaminedefense.dodiserver.users.repository.user

import com.dopaminedefense.dodiserver.users.dto.FriendDto
import com.dopaminedefense.dodiserver.users.dto.ProfileRes
import com.dopaminedefense.dodiserver.users.entity.Users
import java.time.LocalDate
import java.time.LocalDateTime

interface UserDslRepository {
    fun existByEmail(email: String) : Boolean
    fun findByEmail(email: String) : Users?

    fun findUserProfile(email: String): ProfileRes?

    fun findByFriendEmail(friendEmail: String) : Users?

    fun getFriendBlockData(email: String, date: String) : List<FriendDto>
}