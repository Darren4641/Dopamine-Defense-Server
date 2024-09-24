package com.dopaminedefense.dodiserver.block.repository

import com.dopaminedefense.dodiserver.block.entity.Block
import com.dopaminedefense.dodiserver.system.dto.Category
import com.dopaminedefense.dodiserver.system.entity.Operation


interface BlockDslRepository {

    fun getWelcomeMessage(email: String, category: Category) : Operation

    fun getBlock(email: String, date: String) : Block?

    fun getBlockOfMonthByCalculatedIsFalse(email: String, date: String) : List<Block>

    fun getBlockOfMonth(email: String, date: String) : List<Block>

    fun getBlockCountDifference(currentDate: String, previousDate: String): Double
}