package com.dopaminedefense.dodiserver.block.repository

import com.dopaminedefense.dodiserver.block.entity.Block
import org.springframework.data.jpa.repository.JpaRepository

interface BlockRepository : JpaRepository<Block, Long>, BlockDslRepository {

}