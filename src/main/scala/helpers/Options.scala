package helpers

import com.github.nscala_time.time.Imports._
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, Interval}

/**
 * This can be used to configure some of codes behavior
 * Created by Oskari on 7.1.2015.
 */
object Options {
  val dateFormat: String = "dd.MM.YYYY"

  val dayStart: String = "08:00"
  val dayEnd: String = "16:00"

  val printHeader: String = "Monthly Wages:"

  val normalDayLength: Int = 8
  // Hours
  val hourlyWage: Int = 375
  // Cents
  val eveningBonus: Int = 115
  // Cents
  val overtimeBonus: Map[Int, Int] = Map(0 -> 25, 2 -> 50, 4 -> 100) // hours of overtime -> per cent of hourly wage for bonus

  val nameHeader: String = "Person Name"
  val idHeader: String = "Person ID"
  val dateHeader: String = "Date"
  val startHeader: String = "Start"
  val endHeader: String = "End"

  val time24: Boolean = true

  val dateFormatter = DateTimeFormat.forPattern(dateFormat)
  val startOfDay: LocalTime = LocalTime.parse(dayStart)
  val endOfDay: LocalTime = LocalTime.parse(dayEnd)
}
