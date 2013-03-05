package misty.practices.akka.remoting

import akka.actor.{Props, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-4
 * Time: 下午7:50
 */
object ServerActorSystem extends App {
	val config = ConfigFactory.parseString(
		"""
		  |akka {
		  |	actor {
		  |		provider = "akka.remote.RemoteActorRefProvider"
		  |	}
		  |	remote {
		  |		transport = "akka.remote.netty.NettyRemoteTransport"
		  |		netty {
		  |			hostname = "127.0.0.1"
		  |			port = 2554
		  |		}
		  |	}
		  |}
		""".stripMargin)
	val system = ActorSystem("ServerActorSystem", config)
	println(system)
	val actor = system.actorOf(Props(new Actor {
		def receive = {
			case m => println(s"receive: $m -- from ${sender.path}")
		}
	}), "actor")
	println(actor)
}
