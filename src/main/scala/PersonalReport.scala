import helpers.Options
import org.joda.time.LocalDate

/**
 * Personal report collects work information from single employee
 * Money is handled as cents
 * Created by Oskari on 7.1.2015.
 */


class PersonalReport(val records: List[WorkRecord]) {

  def dailyWage(records: List[WorkRecord]): Int = {
    val totalDayHours = records.foldLeft(0.0)((x: Double, record: WorkRecord) => x + record.dayHours)
    val totalEveningHours = records.foldLeft(0.0)((x: Double, record: WorkRecord) => x + record.eveningHours)
    val overtime = totalDayHours + totalEveningHours - Options.normalDayLength

    val regularWage = totalDayHours * Options.hourlyWage
    val eveningWage = totalEveningHours * (Options.hourlyWage + Options.eveningBonus)

    val overtimeBonus = Options.overtimeLimits.zipWithIndex map { case (limit, i) =>
      if (overtime > limit) (overtime - limit) * Options.overtimeBonus(i) / 100.0 else 0.0
    } reduce (_ + _)

    (regularWage + eveningWage + overtimeBonus).round.toInt
  }

  lazy val totalWage = {
    records.groupBy(_.date).foldLeft(0)((x: Int, y: (LocalDate, List[WorkRecord])) => x + dailyWage(y._2))
  }

  lazy val totalHours = {
    records.foldLeft(0.0)((hours: Double, record: WorkRecord) => hours + record.length)
  }

}
