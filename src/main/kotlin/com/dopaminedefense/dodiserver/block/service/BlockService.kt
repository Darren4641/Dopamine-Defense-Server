package com.dopaminedefense.dodiserver.block.service

import com.dopaminedefense.dodiserver.block.dto.CalendarRes
import com.dopaminedefense.dodiserver.block.dto.HomeRes

interface BlockService {
    fun getHome(email: String) : HomeRes

    fun getCalendar(email: String, date: String) : CalendarRes
}