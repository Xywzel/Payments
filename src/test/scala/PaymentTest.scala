import org.scalatest._

/**
 * Created by Oskari on 8.1.2015.
 */
class PaymentTest extends FlatSpec with Matchers {
  "A cents to print" should "return correct printing for default options" in {
    Payment.centsToPrint(0) should equal("$0.00")
    Payment.centsToPrint(100) should equal("$1.00")
    Payment.centsToPrint(10) should equal("$0.10")
    Payment.centsToPrint(110) should equal("$1.10")
    Payment.centsToPrint(30000) should equal("$300.00")
  }
}
