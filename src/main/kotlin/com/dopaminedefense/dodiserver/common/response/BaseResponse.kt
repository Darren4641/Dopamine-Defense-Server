package com.example.kopring.common.response

import com.example.kopring.common.status.ResultCode

data class BaseResponse<T> (
    val resultCode : String = ResultCode.SUCCESS.code,
    val message : String = ResultCode.SUCCESS.message,
    val data : T? = null
)