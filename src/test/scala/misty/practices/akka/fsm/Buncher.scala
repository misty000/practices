package misty.practices.akka.fsm

import akka.actor._
import concurrent.duration.DurationLong
import java.util.concurrent.TimeUnit
import akka.actor.FSM.{UnsubscribeTransitionCallBack, SubscribeTransitionCallBack}

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-5
 * Time: 下午4:34
 */
object Buncher extends App {
	val system = ActorSystem("Buncher")
	val testActor = system.actorOf(Props(new Actor with ActorLogging {
		def receive = {
			case m => log.info("test actor received: {}", m)
		}
	}), "testActor")
	val buncher = system.actorOf(Props(new Buncher), "buncher")
	//注册外部监控
	buncher ! SubscribeTransitionCallBack(testActor)

	buncher ! SetTarget(testActor)
	buncher ! Queue(42)
	TimeUnit.MILLISECONDS.sleep(500)
	buncher ! Queue(43)
	TimeUnit.MILLISECONDS.sleep(500)
	buncher ! Queue(44)
	TimeUnit.MILLISECONDS.sleep(500)
	buncher ! Flush
	buncher ! Queue(45)
	TimeUnit.MILLISECONDS.sleep(500)
	buncher ! Flush
	buncher ! "go"

	//注销外部监控
	buncher ! UnsubscribeTransitionCallBack(testActor)
	buncher ! Stop


	TimeUnit.SECONDS.sleep(1)
	system.stop(buncher)
	system.shutdown()
}

class Buncher extends Actor with LoggingFSM[State, Data] {
	override def logDepth = 12

	startWith(Idle, Uninitialized)

	when(Idle) {
		case Event(SetTarget(ref), Uninitialized) => stay using Todo(ref, Vector.empty)
		case Event(Stop, _) => stop()
	}

	when(Active, stateTimeout = 1.second) {
		case Event(Flush | StateTimeout, t: Todo) =>
			goto(Idle) using t.copy(queue = Vector.empty) replying "goto idle"
		case Event(Stop, _) => stop()
	}

	//不叠加
	whenUnhandled {
		case Event(Queue(obj), t@Todo(_, v)) =>
			goto(Active) using t.copy(queue = v :+ obj)

		case Event(e, s) =>
			log.warning("received unhandled request `{}` in state `{}`/`{}`", e, stateName, s)
			stay()
	}

	//叠加
	onTransition {
		case Active -> Idle =>
			// stateData     - 旧状态数据
			// nextStateData - 新状态数据
			this.stateData match {
				case Todo(ref, queue) => ref ! Batch(queue)
			}
		case Idle -> Active =>
			//创建定时器
			setTimer("timeout", Tick, 1.second, true)
		case Active -> _ =>
			//查询定时器状态
			timerActive_?("timeout")
			//取消定时器
			cancelTimer("timeout")
		case x -> Idle => log.info("entering Idle from `{}`", x)
	}

	// onTransition(handler _)
	// private def handler(from: S, to: S) {
	// }

	override def postStop() {
		//postStop默认调用onTermination
		super.postStop()
		log.info("stopped")
	}

	//不叠加
	onTermination {
		case StopEvent(reason, state, data) => log.info("on termination: `{}`", reason)
	}

	initialize
	//终止FSM
	//stop(reason = Normal|Shutdown|Failure(reason), data)
}

case object Tick

// received events
case class SetTarget(ref: ActorRef)

case class Queue(obj: Any)

case object Flush

case object Stop

// sent events
case class Batch(obj: Seq[Any])

// states
sealed trait State

case object Idle extends State

case object Active extends State


sealed trait Data

case object Uninitialized extends Data

case class Todo(target: ActorRef, queue: Seq[Any]) extends Data
