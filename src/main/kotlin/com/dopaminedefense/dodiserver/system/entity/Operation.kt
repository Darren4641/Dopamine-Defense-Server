package com.dopaminedefense.dodiserver.system.entity

import com.dopaminedefense.dodiserver.common.entity.BaseEntity
import com.dopaminedefense.dodiserver.system.dto.Category
import com.dopaminedefense.dodiserver.system.dto.Language
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "operation")
class Operation (
    val type: String,
    val context: String,
    @Enumerated(EnumType.STRING)
    val category: Category,
    val languageCode: String

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}