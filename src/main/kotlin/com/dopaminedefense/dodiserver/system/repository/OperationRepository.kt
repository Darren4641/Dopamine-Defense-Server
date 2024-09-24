package com.dopaminedefense.dodiserver.system.repository

import com.dopaminedefense.dodiserver.system.entity.Operation
import org.springframework.data.jpa.repository.JpaRepository

interface OperationRepository : JpaRepository<Operation, Long> {
    fun findByContextAndLanguageCode(context: String, language: String) : Operation?
}