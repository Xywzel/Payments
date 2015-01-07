package helpers

import com.github.nscala_time.time.Imports._
import org.joda.time.format.DateTimeFormat

/**
 * This can be used to configure some of codes behavior
 * Created by Oskari on 7.1.2015.
 */
object Options {
  val dateFormat: String = "dd.MM.YYYY"
  val decimalSeparator: String = "."
  val currencySymbol: String = "$"

  val dayStart: String = "08:00"
  val dayEnd: String = "16:00"

  val printHeader: String = "Monthly Wages:"

  val normalDayLength: Int = 8
  // Hours
  val hourlyWage: Int = 375
  // Cents
  val eveningBonus: Int = 115
  // Cents
  val overtimeBonus: Vector[Double] = Vector(25.0, 25.0, 50.0)
  // Cumulative bonus for overtime
  val overtimeLimits: Vector[Int] = Vector(0, 2, 4) // Hours of overtime after above is used
  // 0-2 hours of overtime 25% bonus, 2-4 hours -> 25% + 25%, 4- 100%

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
