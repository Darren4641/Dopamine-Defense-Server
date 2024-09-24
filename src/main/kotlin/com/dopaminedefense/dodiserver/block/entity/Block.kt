package com.dopaminedefense.dodiserver.block.entity

import com.dopaminedefense.dodiserver.common.entity.BaseEntity
import com.dopaminedefense.dodiserver.users.dto.CountryCode
import com.dopaminedefense.dodiserver.users.dto.CountryCode.Companion.getYesterdayUtcDateTimeStr
import com.dopaminedefense.dodiserver.users.entity.Users
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "block")
class Block (
    @Column(name = "block_count")
    var blockCount: Int = 0,

    @Column(name = "longest_usage")
    var longestUsage: Int = 0,

    @Column(name = "calculated")
    var calculated: Boolean = false,

    @Column(name = "level")
    var level: Int?,

    @Column(name = "blocks")
    var blocks: String,

    @Column(name = "exceeded_count")
    var exceededCount: Int = 0,

    @Column(name = "action_count")
    var actionCount: Int = 0,

    @Column(name = "detailed_blocks", length = 200)
    var detailedBlocks: String,

    @Column(name = "rest_sec")
    var restSec: Int = 0,

    @Column(name = "rest_min_graph", length = 300)
    var restMinGraph: String,

    @Column(name = "pickup_on_count")
    var pickupOnCount: Int = 0,

    @Column(name = "is_skipped")
    var isSkipped: Boolean = false,

    @Column(name = "local_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var localDate : String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: Users

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun changeCreateDateTime(isYesterday: Boolean) : Block {
        if(isYesterday) {
            this.createdDate = getYesterdayUtcDateTimeStr()
        }
        return this
    }

    companion object {
        val BLOCK_SECOND = 1000
        val BLOCK_SECOND_OF_DOUBLE = 1000.0
    }
}