package models

import org.joda.time.DateTime

import reactivemongo.bson._

import scala.util.Try

case class User( _id : Option[BSONObjectID],
                 age: Int,
                 firstName: String,
                 lastName: String,
                 active: Boolean
                )
import play.api.libs.json.Json
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import reactivemongo.bson._

object User {
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
  // User JSON Format implicit for REST
  implicit val userReads: Reads[User] = (
  (JsPath \ "_id").readNullable[BSONObjectID] and
  (JsPath \ "age").read[Int] and
  (JsPath \ "firstName").read[String] and
  (JsPath \ "lastName").read[String] and
  (JsPath \ "active").read[Boolean]
)(User.apply _)
  implicit val userWrites: Writes[User] = (
  (JsPath \ "_id").writeNullable[BSONObjectID]   and
  (JsPath \ "age").write[Int] and
  (JsPath \ "firstName").write[String] and
  (JsPath \ "lastName").write[String] and
  (JsPath \ "active").write[Boolean]
)(unlift(User.unapply))
  implicit val userFormatFromJSON =Json.fromJson[User]
  implicit val userFormatToJSON =Json.toJson[User]
}
