package com.dopaminedefense.dodiserver.users.repository.user

import com.dopaminedefense.dodiserver.users.dto.UserStatus
import com.dopaminedefense.dodiserver.users.entity.Users
import org.springframework.data.jpa.repository.JpaRepository

interface UsersRepository : JpaRepository<Users, Long>, UserDslRepository {

    fun countByStatus(status: UserStatus) : Long
}