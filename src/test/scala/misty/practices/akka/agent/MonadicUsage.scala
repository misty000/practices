package misty.practices.akka.agent

import akka.actor.ActorSystem
import akka.agent.Agent
import akka.util.Timeout
import concurrent.duration.DurationInt

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-6
 * Time: 下午3:50
 */
object MonadicUsage extends App {
	implicit val system = ActorSystem("MonadicUsage")
	implicit val timeout: Timeout = 5.seconds

	val agent1 = Agent(3)
	println(s"agent1 ${agent1.await}")
	val agent2 = Agent(5)
	println(s"agent2 ${agent2.await}")

	// uses foreach
	var result = 0
	agent1 foreach (v => result = v + 1)
	println(s"result $result")

	// uses map
	val agent3: Agent[Int] = for (value <- agent1) yield 1 + value
	println(s"agent3 ${agent3.await}")

	val agent4 = agent1 map (_ + 1)
	println(s"agent4 ${agent4.await}")

	val agent5: Agent[Int] = for {
		value1 <- agent1
		value2 <- agent2
	} yield 0 + value1 + value2
	println(s"agent5 ${agent5.await}")

	system.shutdown()
}
