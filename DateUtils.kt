package com.smarttaskmanager.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun formatDueDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
