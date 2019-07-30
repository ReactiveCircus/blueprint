package reactivecircus.blueprint.demo.util

import org.amshove.kluent.shouldEqual
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class DateTest {

    @Test
    fun `should convert to formatted String from timestamp`() {
        val pattern = "EEE d MMM 'at' h:mm a"

        // Monday, 25 June 2018 08:30:00
        val timestamp = Calendar.getInstance().apply {
            set(2018, 5, 25, 8, 30)
            timeZone = TimeZone.getDefault()
        }.time.toInstant().toEpochMilli()

        timestamp.toFormattedDateString(pattern) shouldEqual "Mon 25 Jun at 8:30 AM"
    }
}
