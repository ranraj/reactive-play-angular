package json

import models.Plan
import org.joda.time.{DateTimeZone, DateTime}
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID

/**
 * Created by ranjithrajd on 10/4/16.
 */
object MongoImplicits {
  // Reads / Writes implicit for BSONObjectID
  implicit val objectIdRead: Reads[BSONObjectID] =
    (__ \ "$oid").read[String].map { oid =>
      BSONObjectID(oid)
    }
  implicit val objectIdWrite: Writes[BSONObjectID] = new Writes[BSONObjectID] {
    def writes(objectId: BSONObjectID): JsValue = Json.obj(
      "$oid" -> objectId.stringify
    )
  }

  // Read / write implicit for DateTime
  implicit val dateTimeRead: Reads[DateTime] =
    (__ \ "$date").read[Long].map { dateTime =>
      new DateTime(dateTime, DateTimeZone.UTC)
    }

  implicit val dateTimeWrite: Writes[DateTime] = new Writes[DateTime] {
    def writes(dateTime: DateTime): JsValue = Json.obj(
      "$date" -> dateTime.getMillis
    )
  }

  implicit val objectIdFormats = Format(objectIdRead, objectIdWrite)
  implicit val dateTimeFormats = Format(dateTimeRead, dateTimeWrite)

}
