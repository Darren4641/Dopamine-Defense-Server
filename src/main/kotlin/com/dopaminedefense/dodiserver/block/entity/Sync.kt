package com.dopaminedefense.dodiserver.block.entity

import com.dopaminedefense.dodiserver.block.dto.Power
import com.dopaminedefense.dodiserver.block.dto.SyncType
import com.dopaminedefense.dodiserver.common.entity.BaseEntity
import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.convertUtcStrToLocalDateTime
import com.dopaminedefense.dodiserver.users.entity.Users
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import org.springframework.data.jpa.domain.support.AuditingEntityListener


@Entity
@DynamicUpdate
@EntityListeners(AuditingEntityListener::class)
@Table(name = "sync")
class Sync (
    @Column(name = "`interval`")
    val `interval`: Int,

    @Column(name = "pickup", length = 50)
    @Enumerated(EnumType.STRING)
    val pickup: Power? = null,

    @Column(name = "type", length = 50)
    @Enumerated(EnumType.STRING)
    val type: SyncType? = null,

    @Column(name = "is_sync")
    var isSync: Boolean,

    @Column(name = "local_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var localDate : String? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: Users,

    ) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun changeCreateDateTime(createdDate: String) {
        this.localDate = convertUtcStrToLocalDateTime(createdDate, CountryCode.valueOf(user.countryCode!!))
        super.createdDate = createdDate
    }

    fun syncComplete() {
        isSync = true
    }
}