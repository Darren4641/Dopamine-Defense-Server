package com.dopaminedefense.dodiserver.alarm.service

import com.slack.api.Slack
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SlackService (
    @Value("\${slack.url}")
    val webhookUrl: String
) {

    fun sendSlackMessage(text: String) {
        val slack = Slack.getInstance()
        val payload = "{\"text\":\"" + text + "\"}";

        try {
            val response = slack.send(webhookUrl, payload)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}