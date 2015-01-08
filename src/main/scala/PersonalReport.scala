import helpers.Options
import org.joda.time.LocalDate

/**
 * Personal report collects work information from single employee
 * Wages are rounded daily to closest cent, times are calculated with
 * about accuracy of one minute.
 * Money is handled as cents
 * Created by Oskari on 7.1.2015.
 */


class PersonalReport(val allRecords: List[WorkRecord]) {

  def totalDayHours(records: List[WorkRecord]): Double =
    records.foldLeft(0.0)((x: Double, record: WorkRecord) => x + record.dayHours)

  def totalEveningHours(records: List[WorkRecord]): Double =
    records.foldLeft(0.0)((x: Double, record: WorkRecord) => x + record.eveningHours)

  def overTime(records: List[WorkRecord]): Double =
    totalDayHours(records) + totalEveningHours(records) - Options.normalDayLength

  def dailyWage(records: List[WorkRecord]): Int = {
    val overtime = overTime(records)
    val regularWage = totalDayHours(records) * Options.hourlyWage
    val eveningWage = totalEveningHours(records) * (Options.hourlyWage + Options.eveningBonus)

    val overtimeBonus = Options.overtimeLimits.zipWithIndex map { case (limit, i) =>
      if (overtime > limit) (overtime - limit) * Options.hourlyWage * Options.overtimeBonus(i) / 100.0 else 0.0
    } reduce (_ + _)

    (regularWage + eveningWage + overtimeBonus).round.toInt
  }

  lazy val totalWage = {
    allRecords.groupBy(_.date).foldLeft(0)((x: Int, y: (LocalDate, List[WorkRecord])) => x + dailyWage(y._2))
  }

  lazy val totalHours = {
    allRecords.foldLeft(0.0)((hours: Double, record: WorkRecord) => hours + record.length)
  }

}
