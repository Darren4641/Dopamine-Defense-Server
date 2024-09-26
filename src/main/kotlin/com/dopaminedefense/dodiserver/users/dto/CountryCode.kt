package com.dopaminedefense.dodiserver.users.dto

import java.time.*
import java.time.format.DateTimeFormatter

enum class CountryCode (
  val zone: String
) {
    KR("Asia/Seoul"),
    US("America/New_York"),
    JP("Asia/Tokyo"),
    CN("Asia/Shanghai"),
    DE("Europe/Berlin"),
    FR("Europe/Paris");

    companion object {
        val utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX")
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fun getTodayUtcDateTimeStr() : String = OffsetDateTime.now(ZoneOffset.UTC).format(CountryCode.dateTimeFormatter)

        fun getYesterdayUtcDateTimeStr() : String = OffsetDateTime.now(ZoneOffset.UTC).minusDays(1).format(CountryCode.dateTimeFormatter)

        fun getTodayUtcDateStr() : String = OffsetDateTime.now(ZoneOffset.UTC).format(CountryCode.dateFormatter)

        fun getYesterdayUtcDateStr() : String = OffsetDateTime.now(ZoneOffset.UTC).minusDays(1).format(CountryCode.dateFormatter)

        fun getTodayLocalDateTimeStr(countryCode: CountryCode) : String = LocalDateTime.now(ZoneId.of(countryCode.zone)).format(dateFormatter).format(dateTimeFormatter)

        fun getTodayLocalDateStr(countryCode: CountryCode) : String = LocalDateTime.now(ZoneId.of(countryCode.zone)).format(dateFormatter)

        fun getYesterdayLocalDateStr(countryCode: CountryCode) : String = LocalDateTime.now(ZoneId.of(countryCode.zone)).minusDays(1).format(dateFormatter)


        fun convertUtcToLocalTime(utcTime: LocalDateTime, countryCode: CountryCode): String {
            val utcZonedTime = ZonedDateTime.of(utcTime, ZoneId.of("UTC"))
            val localZonedTime = utcZonedTime.withZoneSameInstant(ZoneId.of(countryCode.zone))
            return localZonedTime.format(dateTimeFormatter)
        }

        fun convertUtcStrToLocalDateTime(utcTime: String, countryCode: CountryCode): String {
            val dateToString = LocalDateTime.parse(utcTime, dateTimeFormatter)
            val utcZonedTime = ZonedDateTime.of(dateToString, ZoneId.of("UTC"))
            val localZonedTime = utcZonedTime.withZoneSameInstant(ZoneId.of(countryCode.zone))
            return localZonedTime.format(dateTimeFormatter)
        }

        fun convertUtcStrToLocalDate(utcTime: String, countryCode: CountryCode): String {
            val dateToString = LocalDateTime.parse(utcTime, dateFormatter)
            val utcZonedTime = ZonedDateTime.of(dateToString, ZoneId.of("UTC"))
            val localZonedTime = utcZonedTime.withZoneSameInstant(ZoneId.of(countryCode.zone))
            return localZonedTime.format(dateFormatter)
        }

        fun convertLocalToUtcDate(date: String, countryCode: CountryCode) : String{
            val localDateTime: LocalDateTime = LocalDateTime.parse(date, dateTimeFormatter)
            val localZonedDateTime: ZonedDateTime = localDateTime.atZone(ZoneId.of(countryCode.zone))
            val utcZonedDateTime = localZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))

            val utcDate: LocalDate = utcZonedDateTime.toLocalDate()

            return utcDate.format(dateFormatter)
        }

        fun toDateTime(dateTimeString: String): LocalDateTime {
            // 문자열을 LocalDateTime으로 파싱
            return LocalDateTime.parse(dateTimeString, dateTimeFormatter)
        }


    }


}