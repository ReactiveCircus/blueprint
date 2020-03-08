package reactivecircus.blueprint.demo.util

import org.amshove.kluent.shouldEqual
import org.junit.Test
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class DateTest {

    @Test
    fun `timestamp can be converted to a formatted date String given a date pattern`() {
        val pattern = "EEE d MMM 'at' h:mm a"

        // Monday, 25 June 2018 08:30:00
        val timestamp = Calendar.getInstance().apply {
            set(2018, 5, 25, 8, 30)
            timeZone = TimeZone.getDefault()
        }.time.toInstant().toEpochMilli()

        timestamp.toFormattedDateString(pattern, Locale.ENGLISH) shouldEqual "Mon 25 Jun at 8:30 AM"
    }
}
