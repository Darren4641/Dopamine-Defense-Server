package com.dopaminedefense.dodiserver.alarm.entity

import com.dopaminedefense.dodiserver.common.entity.BaseEntity
import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.entity.Users
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "alarm")
class Alarm (
    @Column(length = 320)
    val title: String,

    @Column(columnDefinition = "TEXT")
    val content: String,

    @Column(name = "is_read")
    val isRead: Boolean = false,

    @Column(name = "is_send")
    val isSend: Boolean = false,

    @Column(length = 100)
    val sender: String,

    @Column(name = "send_date")
    val sendDate: String? = null,

    @Column(name = "local_date")
    var localDate : String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: Users

    ) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

}