/**
 * This file contains class for storing and handling a single row of work record.
 * The companion object can be used to create many of these from single csv file
 * Created by Oskari on 6.1.2015.
 */

import java.io.File
import java.util.Date

import com.github.nscala_time.time.Imports._
import com.github.tototoshi.csv.CSVReader
import helpers.Options

class WorkRecord(val person: Person, val time: Interval) {

  def lenght: Int = {
    time.duration.getStandardHours.toInt
  }

  def dayHours: Int = {
    val daysWorkTime = time.start.toLocalDate.toDateTime(Options.startOfDay) to time.start.toLocalDate.toDateTime(Options.endOfDay)
    time.overlap(daysWorkTime).duration.
    0
  }

  def eveningHours: Int = {
    0
  }

  def isBefore(moment: Date): Boolean = {
    time.start.date.before(moment)
  }
}

object WorkRecord {

  def parseDate(string: String): Date = {
    DateTime.parse(string, Options.dateFormatter).date
  }

  def fromFile(fileName: String): List[WorkRecord] = {
    val reader = CSVReader.open(new File(fileName))
    val rows = reader.allWithHeaders().map(fromMap)
    reader.close()
    rows
  }

  def fromMap(record: Map[String, String]): WorkRecord = {
    println(Options.nameHeader)
    println(record(Options.idHeader))
    val person = new Person(record(Options.nameHeader), record(Options.idHeader))
    val time =
      intervalFromStrings(
        record(Options.dateHeader),
        record(Options.startHeader),
        record(Options.endHeader))
    new WorkRecord(person, time)
  }

  def intervalFromStrings(date: String, start: String, end: String): Interval = {
    val startTime = LocalTime.parse(start)
    val endTime = LocalTime.parse(end)
    val startDate = LocalDate.parse(date, Options.dateFormatter)
    val endDate = if (endTime < startTime) startDate + 1.days else startDate
    startDate.toDateTime(startTime) to endDate.toDateTime(endTime)
  }
}
