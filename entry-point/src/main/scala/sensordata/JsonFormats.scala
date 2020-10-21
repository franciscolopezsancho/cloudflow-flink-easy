package sensordata

import java.time.Instant
import java.util.UUID

import scala.util.Try

import spray.json._

trait UUIDJsonSupport extends DefaultJsonProtocol {
  implicit object UUIDFormat extends JsonFormat[UUID] {
    def write(uuid: UUID) = JsString(uuid.toString)

    def read(json: JsValue): UUID = json match {
      case JsString(uuid) ⇒ Try(UUID.fromString(uuid)).getOrElse(deserializationError(s"Expected valid UUID but got '$uuid'."))
      case other          ⇒ deserializationError(s"Expected UUID as JsString, but got: $other")
    }
  }
}

object SensorDataJsonSupport extends DefaultJsonProtocol with UUIDJsonSupport {
  implicit val sensorDataFormat = jsonFormat2(SensorData.apply)
}