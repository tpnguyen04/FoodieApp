package com.example.foodieapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TimeUtils {

    // Hàm nhận vào chuỗi ngày giờ và format lại
    fun formatDateTime(inputDateTime: String): String {
        try {
            // Định dạng chuỗi đầu vào
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Đảm bảo xử lý múi giờ Z (UTC)

            // Parse chuỗi ngày giờ thành đối tượng Date
            val date: Date = inputFormat.parse(inputDateTime) ?: return ""

            // Định dạng chuỗi đầu ra
            val outputFormat = SimpleDateFormat("HH:mm:ss | dd-MM-yyyy", Locale.getDefault())

            // Trả về chuỗi đã format
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}