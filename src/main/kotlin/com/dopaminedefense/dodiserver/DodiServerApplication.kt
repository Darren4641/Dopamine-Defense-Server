package com.dopaminedefense.dodiserver

import com.dopaminedefense.dodiserver.common.properties.S3Properties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableCaching
@EntityScan("com.dopaminedefense")
@EnableJpaRepositories("com.dopaminedefense.*")
@SpringBootApplication
@EnableConfigurationProperties(S3Properties::class)
class DodiServerApplication

fun main(args: Array<String>) {
    runApplication<DodiServerApplication>(*args)
}
