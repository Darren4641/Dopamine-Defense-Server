package com.dopaminedefense.dodiserver.system.dto

import com.dopaminedefense.dodiserver.system.entity.Operation

enum class Category(
    val value: String
) {
    LINK("link"),
    WELCOME_MSG("welcome-msg")
}

enum class Language(
    val value: String
) {
    KO("ko"),
    EN("en")
}

class OperationReq(
    val type: String,
    val context: String,
    val category: Category,
    val languageCode: String
) {
    fun toEntity() : Operation {
        return Operation(
            type = type,
            context = context,
            category = category,
            languageCode = languageCode
        )
    }
}

class OperationRes(
    val type: String,
    val context: String,
    val category: Category,
    val languageCode: String
)