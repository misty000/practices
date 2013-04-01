package misty.practices.spray.io

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-21
 * Time: 下午2:17
 */
object IOClientSample extends App {

	import java.util.concurrent.TimeUnit._
	import scala.concurrent.duration.Duration
	import akka.util.Timeout
	import akka.pattern.ask
	import akka.actor._
	import spray.util._
	import spray.io._

	class EchoClient(_ioBridge: ActorRef) extends IOClient(_ioBridge) {
		var pingSender: Option[ActorRef] = None

		override def receive = before andThen (myReceive orElse super.receive)

		def before: PartialFunction[Any, Any] = {
			case e =>
				log.debug("Received {}", e)
				e
		}

		def myReceive: Receive = {
			case EchoClient.Ping(handle) =>
				pingSender = Some(sender)
				handle.ioBridge ! IOBridge.Send(handle, BufferBuilder("PING").toByteBuffer)

			case IOClient.Received(handle, buffer) =>
				pingSender.foreach(_ ! EchoClient.PingResponse(buffer.drainToString))

			case IOClient.Closed(_, reason) =>
				log.debug("Connection closed: {}", reason)
		}
	}

	object EchoClient {

		case class Ping(handle: Connection) extends Command

		case class PingResponse(response: String) extends Event

	}

	class EchoServer(_ioBridge: ActorRef) extends IOServer(_ioBridge) {
		override def receive = super.receive orElse {
			case IOServer.Received(handle, buffer) if buffer.duplicate.drainToString == "PING" =>
				sender ! IOBridge.Send(handle, BufferBuilder("PONG").toByteBuffer)
				sender ! IOBridge.Close(handle, ConnectionCloseReasons.CleanClose)
		}
	}

	val system = ActorSystem()

	val ioBridge = IOExtension(system).ioBridge()

	val server = system.actorOf(Props(new EchoServer(ioBridge)), "server")
	val client = system.actorOf(Props(new EchoClient(ioBridge)), "client")

	implicit val timeout: Timeout = Duration(1, SECONDS) // timeout for the asks below

	// bind the server to a local port and wait for it to come up
	server.ask(IOServer.Bind("localhost", 46468)).await

	// ask the client to connect to the server and wait for the connection to come up
	val IOClient.Connected(handle) = client.ask(IOClient.Connect("localhost", 46468)).await

	// ping the server and wait for the response
	val EchoClient.PingResponse(response) = client.ask(EchoClient.Ping(handle)).await

	response == "PONG"

	system.shutdown()
}
