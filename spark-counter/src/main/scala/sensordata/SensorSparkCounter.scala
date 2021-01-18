package sensordata


import cloudflow.streamlets._
import cloudflow.streamlets.avro._
import cloudflow.spark.{ SparkStreamlet, SparkStreamletLogic }
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import SensorDataJsonSupport._
import sensordata._

import cloudflow.spark.{ SparkStreamlet, SparkStreamletLogic }
import org.apache.spark.sql.streaming.OutputMode
import spark.implicits._


class SensorSparkCounter extends SparkStreamlet {

  @transient val in  = AvroInlet[SensorData]("in")
  @transient  val out   = AvroOutlet[SensorData]("out")


  def shape = StreamletShape.withInlets(in).withOutlets(out)


  override def createLogic = new SparkStreamletLogic {


   override def buildStreamingQueries = {
  	implicit val encoder: Encoders[SensorData] = Encoders.product[SensorData]
   		println("hello")
      val dataset   = readStream(in)(Encoders.product[SensorData],TypeTag[SensorData])
      dataset.show() 
      val outStream = dataset.filter("count > 5")
      writeStream(outStream, out, OutputMode.Append).toQueryExecution
    }
  }
}
