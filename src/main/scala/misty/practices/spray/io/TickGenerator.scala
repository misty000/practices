package misty.practices.spray.io

import spray.io._
import concurrent.duration.{Duration, DurationLong, FiniteDuration}
import spray.io.TickGenerator.Tick
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-21
 * Time: 下午7:32
 */
object TickGenerator {
	def apply(millis: Long): PipelineStage = apply(millis.millis)

	def apply(period: FiniteDuration): PipelineStage = {
		require(period > Duration.Zero, "period must be positive")
		new PipelineStage {
			def build(context: PipelineContext, commandPL: CPL, eventPL: EPL): Pipelines = {
				new Pipelines {
					val generator = {
						val system = context.connectionActorContext.system
						system.scheduler.schedule(
							initialDelay = period,
							interval = period,
							receiver = context.self,
							message = Tick
						)
					}

					def eventPipeline: (Event) => Unit = ???

					def commandPipeline: (Command) => Unit = ???
				}
			}
		}
	}
}
