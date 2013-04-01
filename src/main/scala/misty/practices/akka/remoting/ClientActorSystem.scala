package misty.practices.akka.remoting

import com.typesafe.config.ConfigFactory
import akka.actor._
import akka.remote.RemoteScope
import java.util.concurrent.TimeUnit

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 下午7:55
 */
object ClientActorSystem extends App {
	val config = ConfigFactory.parseString(
		"""
		  |akka {
		  |	actor {
		  |		# client
		  |		provider = "akka.remote.RemoteActorRefProvider"
		  |
		  |  	# 告知Akka当一个路径为 /sampleActor 的actor被创建时进行响应
		  |   	# i.e. 调用 system.actorOf(Props(...)`, sampleActor)时. 指定的actor不会被直接实例化，
		  |    	# 而是远程actor系统的daemon被要求创建这个actor, 本例中的远程actor系统是akka://ServerActorSystem@127.0.0.1:2555
		  |  	deployment {
		  |   		/sampleActor {
		  |     		remote = "akka://ServerActorSystem@127.0.0.1:2555"
		  |     	}
		  |		}
		  |	}
		  |	remote {
		  |		transport = "akka.remote.netty.NettyRemoteTransport"
		  |		netty {
		  |			hostname = "127.0.0.1"
		  |			port = 2556
		  |		}
		  |	}
		  |}
		""".stripMargin)
	val system = ActorSystem("ClientActorSystem", config)

	{
		val actor = system.actorFor("akka://ServerActorSystem@127.0.0.1:2555/user/actor")
		println(actor)
		actor ! "hello"
	}
	{
		val actor = system.actorOf(Props[SampleActor], "sampleActor")
		actor ! "Pretty slick"
	}
	{
		val address = AddressFromURIString("akka://ServerActorSystem@127.0.0.1:2555")
		val ref = system.actorOf(Props[Echo].withDeploy(Deploy(scope = RemoteScope(address))), "echoActor")
		ref ! "Go !!!"
	}

	TimeUnit.SECONDS.sleep(1)
	system.shutdown()

	class SampleActor extends Actor {
		def receive = {
			case m => println(s"${self.path} ~ Got something: $m   -- from ${sender.path}")
		}
	}

	class Echo extends Actor {
		def receive = {
			case m => println(s"${self.path} ~ echo: $m   -- from ${sender.path}")
		}
	}

}
