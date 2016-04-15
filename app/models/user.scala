package models

import java.util.Date

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTimeZone, DateTime}
import reactivemongo.bson._

case class User( _id : Option[BSONObjectID],
                 age: Int,
                 dob : Date,
                 firstName: String,
                 lastName: String,
                 emailId: String,
                 interest: String,
                 active: Boolean,
                 createdDate: Option[DateTime],
                 updatedDate: Option[DateTime]
                )
import play.api.libs.json.Json
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import reactivemongo.bson._
import json.MongoImplicits._
object User {

  // User JSON Format implicit for REST
  implicit val userReads: Reads[User] = (
  (JsPath \ "_id").readNullable[BSONObjectID] and
  (JsPath \ "age").read[Int] and
  (JsPath \ "dob").read[Date] and
  (JsPath \ "firstName").read[String] and
  (JsPath \ "lastName").read[String] and
  (JsPath \ "emailId").read[String] and
  (JsPath \ "interest").read[String] and
  (JsPath \ "active").read[Boolean] and
  (JsPath \ "createdDate").readNullable[DateTime].map(_.getOrElse(new DateTime())).map(Some(_))  and
  (JsPath \ "updatedDate").readNullable[DateTime].map(_.getOrElse(new DateTime())).map(Some(_))
)(User.apply _)
  val userDateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm a")
  implicit val userWrites: Writes[User] = (
  (JsPath \ "_id").writeNullable[BSONObjectID]   and
  (JsPath \ "age").write[Int] and
  (JsPath \ "dob").write[Date] and
  (JsPath \ "firstName").write[String] and
  (JsPath \ "lastName").write[String] and
  (JsPath \ "emailId").write[String] and
  (JsPath \ "interest").write[String] and
  (JsPath \ "active").write[Boolean] and
  (JsPath \ "createdDate").writeNullable[DateTime] and
  (JsPath \ "updatedDate").writeNullable[DateTime]
)(unlift(User.unapply))

  implicit val userFormatFromJSON =Json.fromJson[User]
  implicit val userFormatToJSON =Json.toJson[User]
}
