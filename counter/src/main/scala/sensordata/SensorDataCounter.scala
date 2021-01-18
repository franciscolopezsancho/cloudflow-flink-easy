package sensordata

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.functions.co._
import org.apache.flink.api.common.state.{ ValueState, ValueStateDescriptor }
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.time.Time
import java.util.concurrent.TimeUnit
import org.apache.flink.util.Collector
import sensordata._

import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro._
import cloudflow.flink._
import scala.concurrent.duration._

class SensorDataCounter extends FlinkStreamlet {
  @transient val in    = AvroInlet[SensorData]("in")
  @transient val shape = StreamletShape(in)

  override def createLogic() = new FlinkStreamletLogic {

    override def buildExecutionGraph =
      readStream(in)
        .keyBy("deviceId")
        .sum(1)
        .map { t =>
          println(t); t
        }

  }
}
// .map{ t => throw new IllegalArgumentException(" This is still Ilegal");t}
