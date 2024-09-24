package com.dopaminedefense.dodiserver.system.service

import com.dopaminedefense.dodiserver.system.dto.OperationReq
import com.dopaminedefense.dodiserver.system.dto.OperationRes

interface OperationService {

    fun saveOperation(operationReq: OperationReq) : OperationRes
}