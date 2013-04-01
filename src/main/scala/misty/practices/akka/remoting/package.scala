package misty.practices.akka


/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-5
 * Time: 下午1:48
 */
package remoting {

trait MathOp

trait MathResult

case class Add(n1: Int, n2: Int) extends MathOp

case class Subtract(n1: Int, n2: Int) extends MathOp

case class AddResult(n1: Int, n2: Int, r: Int) extends MathResult

case class SubtractResult(n1: Int, n2: Int, r: Int) extends MathResult

case class Multiplication(n1: Int, n2: Int) extends MathOp

case class MultiplicationResult(n1: Int, n2: Int, r: Int) extends MathResult

case class Division(n1: Int, n2: Int) extends MathOp

case class DivisionResult(n1: Int, n2: Int, r: Int) extends MathResult

}
