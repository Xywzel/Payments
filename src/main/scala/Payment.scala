/**
 * Created by Oskari on 6.1.2015.
 */

object Payment extends App {
  val fileName = if (args.length > 0) args(0) else "HourList201403.csv"
  val startTime = if (args.length > 1) Option(WorkRecord.parseDate(args(1))) else None
  val endTime = if (args.length > 2) Option(WorkRecord.parseDate(args(2))) else None

  var record = WorkRecord.fromFile(fileName)
  if (startTime.isDefined) record = record.filter(x => x.isBefore(startTime.get))
  if (endTime.isDefined) record = record.filter(x => x.isBefore(endTime.get))

  val recordsByEmployee : Map[Person, List[WorkRecord]] = record.groupBy(_.person)

  println(recordsByEmployee)
}
