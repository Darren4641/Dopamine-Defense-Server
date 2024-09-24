//package com.dopaminedefense.dodiserver.users.entity
//
//import com.dopaminedefense.dodiserver.common.entity.BaseEntity
//import com.dopaminedefense.dodiserver.users.dto.FriendStatus
//import jakarta.persistence.*
//import org.springframework.data.jpa.domain.support.AuditingEntityListener
//
//@Entity
//@EntityListeners(AuditingEntityListener::class)
//@Table(name = "message")
//class Message (
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    val user: Users,
//
//    @Column(name = "target_user", length = 320)
//    val targetUser: String
//
//    val context: String
//) : BaseEntity() {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    var id: Long? = null
//
//    fun updateStatus(status: FriendStatus) {
//        this.status = status
//    }
//
//}