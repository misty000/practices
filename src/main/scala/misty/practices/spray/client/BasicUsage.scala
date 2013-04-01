package misty.practices.spray.client

import akka.actor.{Props, ActorSystem}
import spray.io.IOExtension
import spray.can.client.HttpClient
import spray.client.HttpConduit
import concurrent.{Await, Future}
import spray.http.{HttpRequest, HttpResponse}
import concurrent.duration.DurationLong
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-21
 * Time: 下午8:34
 */
object BasicUsage extends App {
	implicit val system = ActorSystem()
	val ioBridge = IOExtension(system).ioBridge()
	val httpClient = system.actorOf(Props(new HttpClient(ioBridge)))

	val conduit = system.actorOf(
		props = Props(new HttpConduit(httpClient, "www.baidu.com")),
		name = "http-conduit"
	)
	val pipeline = HttpConduit.sendReceive(conduit)
	val response: Future[HttpResponse] = pipeline(HttpRequest())
	response.onComplete(resp => {
		resp.foreach(r => {
			println(r)
		})
	})
	Await.ready(response, 10.seconds)
}
