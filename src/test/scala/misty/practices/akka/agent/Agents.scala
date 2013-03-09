package misty.practices.akka.agent

import akka.actor.ActorSystem
import akka.agent.Agent
import java.util.concurrent.TimeUnit
import concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout
import concurrent.duration.DurationDouble

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13=3=6
 * Time: 下午2:30
 */
object Agents extends App {
	implicit val system = ActorSystem("Agents")
	// 隐式提供ActorSystem
	val agent = Agent(5)
	// 显式提供ActorSystem
	// val agent = Agent(5)(system)

	// 对Agent的更新是异步的

	agent send 7
	println("after send 7 = " + agent())

	agent send (x => {
		println("send " + Thread.currentThread())
		x + 1
	})
	println("after send (_ + 1) = " + agent.get)
	agent send (_ * 2)
	println("after send (_ * 2) = " + agent.get)

	agent sendOff (x => {
		println("sendOff " + Thread.currentThread())
		TimeUnit.SECONDS.sleep(1)
		8
	})
	println("after sendOff = " + agent())

	implicit val timeout: Timeout = 5.seconds
	// 等待send队列完成读取
	val result = agent.await
	println("after await = " + result)

	// val future = agent.future
	// val result = Await.result(future, timeout.duration)

	TimeUnit.SECONDS.sleep(5)
	println("after sleep = " + agent())
	agent.close()
	system.shutdown()
}
