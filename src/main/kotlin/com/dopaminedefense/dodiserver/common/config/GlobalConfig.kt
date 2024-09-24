package com.dopaminedefense.dodiserver.common.config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.time.ZoneOffset
import java.util.*

@Configuration
class GlobalConfig {

    @PostConstruct
    fun setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
    }
}