package misty.practices.env


/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-6
 * Time: 上午9:45
 */
object ReadEnv extends App {
	sys.env.foreach {
		case (k, v) => println(f"$k%-30s -> $v")
	}

	val seq = Seq(1, 2, 3)
	println(seq.last)
}
