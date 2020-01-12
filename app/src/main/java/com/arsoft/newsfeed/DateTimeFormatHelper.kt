package com.arsoft.newsfeed

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class MyDateTimeFormatHelper {

    companion object {

        private const val ONE_DAY = 86400000
        private const val ONE_HOUR = 3600000
        private const val ONE_MINUTE = 60000
        private const val ONE_SECOND = 1000

        fun timeFormat(postDate: Long, currentTime: Long): String {
            val deltaTime = currentTime - postDate

            val simpleTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

            val deltaTimeInSecond = deltaTime / ONE_SECOND
            val deltaTimeInMinutes = deltaTime / ONE_MINUTE
            val deltaTimeInHours = deltaTime / ONE_HOUR

            val secondsLastNumber = deltaTimeInSecond%10
            val minutesLastNumber = deltaTimeInMinutes%10
            val hoursLastNumber = deltaTimeInHours%10

            return if (deltaTime < ONE_DAY) {
                if (deltaTime < ONE_HOUR) {
                    if (deltaTime < ONE_MINUTE) {
                        if (secondsLastNumber == 1L && deltaTimeInSecond != 11L) {
                            "$deltaTimeInSecond секунду назад"
                        } else if ((secondsLastNumber == 2L && deltaTimeInSecond != 12L) ||
                            (secondsLastNumber == 3L && deltaTimeInSecond != 13L)||
                            (secondsLastNumber == 4L && deltaTimeInSecond != 14L))  {
                            "$deltaTimeInSecond секунды назад"
                        } else {
                            "$deltaTimeInSecond секунд назад"
                        }

                    } else if (minutesLastNumber == 1L && deltaTimeInMinutes != 11L) {
                        "$deltaTimeInMinutes минуту назад"
                    } else if ((minutesLastNumber == 2L && deltaTimeInMinutes != 12L) ||
                            (minutesLastNumber == 3L && deltaTimeInMinutes != 13L)||
                            (minutesLastNumber == 4L && deltaTimeInMinutes != 14L))  {
                        "$deltaTimeInMinutes минуты назад"
                    } else {
                        "$deltaTimeInMinutes минут назад"
                    }
                } else if (hoursLastNumber == 1L && deltaTimeInHours != 11L) {
                    "$deltaTimeInHours час назад"
                } else if ((hoursLastNumber == 2L && deltaTimeInHours != 12L) ||
                    (hoursLastNumber == 3L && deltaTimeInHours != 13L)||
                    (hoursLastNumber == 4L && deltaTimeInHours != 14L))  {
                    "$deltaTimeInHours часа назад"
                } else {
                    "$deltaTimeInHours часов назад"
                }
            } else {
                "${simpleTimeFormat.format(postDate)} ${simpleDateFormat.format(postDate)}"
            }
        }
    }
}