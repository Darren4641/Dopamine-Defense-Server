package com.dopaminedefense.dodiserver.users.entity

import com.dopaminedefense.dodiserver.common.entity.BaseEntity
import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.dto.FriendStatus
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "friend")
class Friend (
    @Column(name = "status", length = 30)
    @Enumerated(EnumType.STRING)
    var status: FriendStatus,

    @Column(name = "email", length = 320)
    val email: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_user_id", referencedColumnName = "id")
    val targetUser: Users

) : BaseEntity(CountryCode.valueOf(targetUser.countryCode!!)) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun updateStatus(status: FriendStatus) {
        this.status = status
    }

}