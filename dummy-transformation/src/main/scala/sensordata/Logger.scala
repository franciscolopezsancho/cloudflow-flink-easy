package sensordata

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import cloudflow.akkastream._
import cloudflow.akkastream.util.scaladsl._

import cloudflow.streamlets._
import cloudflow.streamlets.avro._
import SensorDataJsonSupport._
import cloudflow.akkastream.scaladsl.RunnableGraphStreamletLogic
import cloudflow.akkastream.scaladsl.FlowWithCommittableContext

class Logger extends AkkaStreamlet {

  @transient val in  = AvroInlet[SensorData]("in")
  @transient val out = AvroOutlet[SensorData]("out").withPartitioner(RoundRobinPartitioner)

  def shape = StreamletShape.withInlets(in).withOutlets(out)

  override def createLogic = new RunnableGraphStreamletLogic() {

    def log(sensorData: SensorData) =
      system.log.info(s"cisco prefix: $sensorData")

    def flow =
      FlowWithCommittableContext[SensorData]
        .map { sensorData ⇒
          log(sensorData)
          sensorData
        }

     def runnableGraph =
      sourceWithCommittableContext(in)
        .via(flow)
        .to(committableSink(out))
  }
}
