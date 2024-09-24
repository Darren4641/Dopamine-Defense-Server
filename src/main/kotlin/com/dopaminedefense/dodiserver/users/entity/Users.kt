package com.dopaminedefense.dodiserver.users.entity

import com.dopaminedefense.dodiserver.block.entity.Block
import com.dopaminedefense.dodiserver.block.entity.Sync
import com.dopaminedefense.dodiserver.common.entity.BaseEntity
import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.getTodayUtcDateTimeStr
import com.dopaminedefense.dodiserver.users.dto.ProfileReq
import com.dopaminedefense.dodiserver.users.dto.UserStatus
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "users")
class Users (

    @Enumerated(EnumType.STRING)
    var status: UserStatus? = UserStatus.ACTIVE,

    @Column(name = "email", nullable = false, length = 320)
    var email: String,

    @Column(name = "country_code", length = 50)
    var countryCode: String? = null,

    @Column(name = "name", length = 60)
    var name: String? = null,

    @Column(name = "interval_time")
    var intervalTime: Int? = null,

    @Column(name = "level")
    var level: Int? = null,

    @Column(name = "job", length = 100)
    var job: String? = null,

    @Lob
    @Column(name = "image", columnDefinition = "TEXT")
    var image: String? = null,

    @Column(name = "version")
    var version: String? = null,

    @Column(name = "is_onboarding")
    var isOnboarding: Boolean = false,

    @Column(name = "share_status")
    var shareStatus: String? = null,

    @Column(name = "profile_link")
    var profileLink: String? = null,

    @Column(name = "latest_login_time")
    var latestLoginTime: String? = null,

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        targetEntity = Log::class)
    var logs: List<Log>? = emptyList(),

    @OneToMany(
        mappedBy = "targetUser",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        targetEntity = Friend::class)
    var friends: List<Friend>? = emptyList(),

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        targetEntity = Block::class)
    var block: List<Block>? = emptyList(),

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        targetEntity = Sync::class)
    var sync: List<Sync>? = emptyList(),


    ) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null



    fun signIn() {
        this.latestLoginTime = getTodayUtcDateTimeStr()
        this.status = UserStatus.ACTIVE
    }

    fun signOut() {
        this.status = UserStatus.DELETE
    }

    fun onBoarding(profileReq: ProfileReq, shareStatusStr: String) : Users {
        name = profileReq.name
        intervalTime = profileReq.intervalTime.value
        level = profileReq.level.value
        job = profileReq.job
        image = profileReq.image
        version = profileReq.version
        isOnboarding = true
        shareStatus = shareStatusStr
        profileLink = profileReq.profileLink
        return this
    }
}