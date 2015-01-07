/**
 * Personal report collects work information from single employee
 * Created by Oskari on 7.1.2015.
 */

object PersonalReport {
  def apply(person: Person, workList: List[WorkRecord]): PersonalReport = {
    new PersonalReport(person, 0, 0)
  }
}

class PersonalReport(private val person: Person, private val wage: Int, private val hours : Int) {
  def +(line: WorkRecord): PersonalReport = {
    new PersonalReport(person, wage, hours)
  }
}
