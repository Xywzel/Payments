import com.github.nscala_time.time.Imports._
import org.scalatest._

/** Tests for PersonalReport class
  * Created by Oskari on 8.1.2015.
  */
class PersonalReportSpec extends FlatSpec with Matchers {

  "A Personal Report" should "have length of single work record interval" in {
    val record = new WorkRecord("A", "1", DateTime.parse("2014-12-1T08:00:00") to DateTime.parse("2014-12-1T16:00:00"))
    val report = new PersonalReport(List(record))
    report.totalHours should equal(8.0)
    report.totalWage should equal(8.0 * 375)
  }

  it should "have payment correctly for 1 hour of night time" in {
    val record = new WorkRecord("A", "1", DateTime.parse("2014-12-1T09:00:00") to DateTime.parse("2014-12-1T17:00:00"))
    val report = new PersonalReport(List(record))
    report.totalHours should equal(8.0)
    report.totalWage should equal(7.0 * 375 + 1.0 * (375 + 115))
  }

  it should "have payment correctly for 7 hours of night time" in {
    val record = new WorkRecord("A", "1", DateTime.parse("2014-12-1T15:00:00") to DateTime.parse("2014-12-1T23:00:00"))
    val report = new PersonalReport(List(record))
    report.totalHours should equal(8.0)
    report.totalWage should equal(1.0 * 375 + 7.0 * (375 + 115))
  }

  it should "have payment correctly for 8 hours of night time" in {
    val record = new WorkRecord("A", "1", DateTime.parse("2014-12-1T16:00:00") to DateTime.parse("2014-12-2T00:00:00"))
    val report = new PersonalReport(List(record))
    report.totalHours should equal(8.0)
    report.totalWage should equal(8.0 * (375 + 115))
  }

  it should "have payment correctly for overtime at day" in {
    val record = new WorkRecord("A", "1", DateTime.parse("2014-12-1T08:00:00") to DateTime.parse("2014-12-1T17:00:00"))
    val report = new PersonalReport(List(record))
    report.totalHours should equal(9.0)
    report.totalWage should equal((8.0 * 375 + 1.25 * 375 + 1.0 * 115).round)
  }

  it should "have correct payment for multiple days" in {
    val record1 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T08:00:00") to DateTime.parse("2014-12-1T16:00:00")) //8
    val record2 = new WorkRecord("A", "1", DateTime.parse("2014-12-2T08:00:00") to DateTime.parse("2014-12-2T16:00:00")) //8
    val record3 = new WorkRecord("A", "1", DateTime.parse("2014-12-3T08:00:00") to DateTime.parse("2014-12-3T16:00:00")) //8
    val record4 = new WorkRecord("A", "1", DateTime.parse("2014-12-4T08:00:00") to DateTime.parse("2014-12-4T16:00:00")) //8
    val report = new PersonalReport(List(record1, record2, record3, record4))
    report.totalHours should equal(4.0 * 8.0)
    report.totalWage should equal((8.0 * 375 * 4).round)
  }

  "A TotalDayHours" should "have combined length of intervals during day hours" in {
    val record1 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T04:00:00") to DateTime.parse("2014-12-1T12:00:00"))
    val record2 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T12:00:00") to DateTime.parse("2014-12-1T20:00:00"))
    val record3 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T13:00:00") to DateTime.parse("2014-12-1T20:00:00"))
    val record4 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T04:00:00") to DateTime.parse("2014-12-1T11:00:00"))
    val report = new PersonalReport(List(record1))
    report.totalDayHours(List(record1, record2)) should equal(8.0)
    report.totalDayHours(List(record1, record3)) should equal(7.0)
    report.totalDayHours(List(record4, record2)) should equal(7.0)
  }

  "A TotalEveningHours" should "have combined length of intervals during night hours" in {
    val record1 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T07:00:00") to DateTime.parse("2014-12-1T12:00:00")) //1
    val record2 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T12:00:00") to DateTime.parse("2014-12-1T17:00:00")) //1
    val record3 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T15:00:00") to DateTime.parse("2014-12-1T20:00:00")) //4
    val record4 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T01:00:00") to DateTime.parse("2014-12-1T12:00:00")) //7
    val report = new PersonalReport(List(record1))
    report.totalEveningHours(List(record1, record2)) should equal(2.0)
    report.totalEveningHours(List(record1, record3)) should equal(5.0)
    report.totalEveningHours(List(record4, record2)) should equal(8.0)
  }

  "A OverTime" should "total length of intervals - 8 hours" in {
    val record1 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T01:00:00") to DateTime.parse("2014-12-1T09:00:00")) //8
    val record2 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T10:00:00") to DateTime.parse("2014-12-1T11:00:00")) //1
    val record3 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T12:00:00") to DateTime.parse("2014-12-1T20:00:00")) //8
    val record4 = new WorkRecord("A", "1", DateTime.parse("2014-12-1T21:00:00") to DateTime.parse("2014-12-2T03:00:00")) //6
    val report = new PersonalReport(List(record1))
    report.overTime(List(record1)) should equal(0.0)
    report.overTime(List(record2)) should equal(-7.0)
    report.overTime(List(record1, record2)) should equal(1.0)
    report.overTime(List(record1, record2, record3, record4)) should equal(15.0)
  }

}
