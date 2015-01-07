import helpers.Options

/**
 * This is a application for calculating wage with very simple hourly wage system
 * Takes as a parameter a .csv file with the hours and optionally a starting and
 * end dates, if omitted, whole file will be used.
 * Created by Oskari on 6.1.2015.
 */

object Payment extends App {
  def centsToPrint(x: Int): String = Options.currencySymbol + x / 100 + Options.decimalSeparator + x % 100

  val fileName = if (args.length > 0) args(0) else "HourList201403.csv"
  val startTime = if (args.length > 1) Option(WorkRecord.parseDate(args(1))) else None
  val endTime = if (args.length > 2) Option(WorkRecord.parseDate(args(2))) else None

  var record = WorkRecord.fromFile(fileName)
  if (startTime.isDefined) record = record.filter(x => x.isBefore(startTime.get))
  if (endTime.isDefined) record = record.filter(x => x.isBefore(endTime.get))

  val recordsByEmployee: Map[(String, String), List[WorkRecord]] = record.groupBy(_.worker)
  val wagesByEmployee: Iterable[(String, String, PersonalReport)] = recordsByEmployee.map(x => (x._1._1, x._1._2, new PersonalReport(x._2)))

  println(Options.printHeader + " " + startTime)
  for (x <- wagesByEmployee) {
    println(x._2 + ", " + x._1 + ", " + centsToPrint(x._3.totalWage))
  }
}
