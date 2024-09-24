package com.dopaminedefense.dodiserver.users.repository.user

import com.dopaminedefense.dodiserver.users.entity.Friend
import org.springframework.data.jpa.repository.JpaRepository

interface FriendRepository : JpaRepository<Friend, Long> {
    fun existsByEmailAndTargetUser_Email(email: String, friendEmail: String) : Boolean

    fun existsByEmailAndTargetUser_Id(email: String, friendId: Long) : Boolean

    fun findByEmailAndTargetUser_Email(email: String, friendEmail: String) : Friend?
}