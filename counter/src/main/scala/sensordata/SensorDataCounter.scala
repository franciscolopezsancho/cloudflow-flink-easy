package sensordata

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.functions.co._
import org.apache.flink.api.common.state.{ ValueState, ValueStateDescriptor }
import org.apache.flink.util.Collector
import sensordata._

import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro._
import cloudflow.flink._

class SensorDataCounter extends FlinkStreamlet {
  val in    = AvroInlet[SensorData]("in")
  val shape = StreamletShape(in)

  override def createLogic() = new FlinkStreamletLogic {

    override def buildExecutionGraph =
      readStream(in).keyBy("deviceId").sum(1).print()

  }
}