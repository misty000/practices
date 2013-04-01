package misty.practices.spray.io

import java.net.InetSocketAddress
import spray.io.{Event, Command}
import spray.io.IOBridge.{Handle, Key}
import spray.util.{IOClosed, ClosedEventReason, CloseCommandReason}
import java.nio.ByteBuffer

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-21
 * Time: 下午2:16
 */
object IOBridgeSample {

	// Commands
	case class Bind(address: InetSocketAddress, backlog: Int, tag: Any = {}) extends Command

	case class Unbind(bindingKey: Key) extends Command

	case class Connect(remoteAddress: InetSocketAddress,
					   localAddress: Option[InetSocketAddress] = None,
					   tag: Any = {}) extends Command

	case object GetStats extends Command

	trait ConnectionCommand extends Command {
		def handle: Handle
	}

	case class Register(handle: Handle) extends ConnectionCommand

	case class Close(handle: Handle, reason: CloseCommandReason) extends ConnectionCommand

	case class Send(handle: Handle,
					buffers: Seq[ByteBuffer],
					ack: Option[Any] = None) extends ConnectionCommand

	case class StopReading(handle: Handle) extends ConnectionCommand

	case class ResumeReading(handle: Handle) extends ConnectionCommand

	object Connect {
		def apply(host: String, port: Int): Connect =
			apply(host, port, {})

		def apply(host: String, port: Int, tag: Any): Connect =
			Connect(new InetSocketAddress(host, port), None, tag)
	}

	object Send {
		def apply(handle: Handle, buffer: ByteBuffer): Send =
			apply(handle, buffer, None)

		def apply(handle: Handle, buffer: ByteBuffer, ack: Option[Any]): Send =
			new Send(handle, buffer :: Nil, ack)
	}

	// Events
	case class Bound(bindingKey: Key, tag: Any) extends Event

	case class Unbound(bindingKey: Key, tag: Any) extends Event

	case class Connected(key: Key, tag: Any) extends Event

	case class Closed(handle: Handle, reason: ClosedEventReason) extends Event with IOClosed

	case class Received(handle: Handle, buffer: ByteBuffer) extends Event

}
