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
    report.totalWage should equal(8.0 * 375 + 1.25 * 375 + 1.0 * 115)
  }

}
