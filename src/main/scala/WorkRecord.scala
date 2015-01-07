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

class WorkRecord(val name: String, val id: String, val time: Interval) {

  def worker: (String, String) = {
    (name, id)
  }

  // Hour has 60 minutes, in accuracy of work hours, below is ok
  def hours(interval: Interval): Double = {
    val hours = interval.duration.getStandardHours
    val mins = interval.duration.getStandardMinutes - 60 * hours
    hours.toDouble + mins.toDouble / 60.0
  }

  def length: Double = {
    hours(time)
  }

  // The work shift might start before midnight and last till day hours
  def dayHours: Double = {
    val today = date.toDateTime(Options.startOfDay) to date.toDateTime(Options.endOfDay)
    val tomorrow = (date + 1.day).toDateTime(Options.startOfDay) to (date + 1.day).toDateTime(Options.endOfDay)
    0.0 + {
      if (time.overlaps(today)) hours(time.overlap(today)) else 0.0
    } + {
      if (time.overlaps(tomorrow)) hours(time.overlap(tomorrow)) else 0.0
    }
  }

  // Everything that is not day hours
  def eveningHours: Double = {
    length - dayHours
  }

  def date: LocalDate = {
    time.start.toLocalDate
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
    val name = record(Options.nameHeader)
    val id = record(Options.idHeader)
    val time =
      intervalFromStrings(
        record(Options.dateHeader),
        record(Options.startHeader),
        record(Options.endHeader))
    new WorkRecord(name, id, time)
  }

  def intervalFromStrings(date: String, start: String, end: String): Interval = {
    val startTime = LocalTime.parse(start)
    val endTime = LocalTime.parse(end)
    val startDate = LocalDate.parse(date, Options.dateFormatter)
    val endDate = if (endTime < startTime) startDate + 1.days else startDate
    startDate.toDateTime(startTime) to endDate.toDateTime(endTime)
  }
}
