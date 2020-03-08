package reactivecircus.blueprint.demo.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Converts the timestamp to a formatted String
 */
fun Long.toFormattedDateString(pattern: String, locale: Locale = Locale.getDefault()): String {
    return SimpleDateFormat(pattern, locale).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date(this))
}
