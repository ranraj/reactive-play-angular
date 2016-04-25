
package models

import java.util.{Date, Comparator}

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTimeZone, DateTime}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import json.MongoImplicits._
case class Plan( override var _id : Option[BSONObjectID],
                 var title: Option[String],
                 content: String,
                 var store:Option[List[String]],
                 active:Option[Boolean]=Some(true),
                 var comments:Option[Array[String]]=Some(Array.empty[String]),
                 var discussion:Option[Array[String]]=Some(Array.empty[String]),
                 override var created: Option[DateTime],
                 override var updated: Option[DateTime]) extends TemporalModel {
}
case class Message(content:String ,author:String )
object Plan {
  implicit val userReads: Reads[Plan] = (
  (JsPath \ "_id").readNullable[BSONObjectID] and
  (JsPath \ "title").readNullable[String] and
  (JsPath \ "content").read[String] and
  (JsPath \ "store").readNullable[List[String]] and
  (JsPath \ "active").readNullable[Boolean].map(_.getOrElse(true)).map(Some(_)) and
  (JsPath \ "comments").readNullable[Array[String]] and
  (JsPath \ "discussion").readNullable[Array[String]] and
  (JsPath \ "created").readNullable[DateTime] and
  (JsPath \ "updated").readNullable[DateTime]
)(Plan.apply _)

  implicit val userWrites: Writes[Plan] = (
  (JsPath \ "_id").writeNullable[BSONObjectID] and
  (JsPath \ "title").writeNullable[String] and
  (JsPath \ "content").write[String] and
  (JsPath \ "store").writeNullable[List[String]] and
  (JsPath \ "active").writeNullable[Boolean] and
  (JsPath \ "comments").writeNullable[Array[String]] and
  (JsPath \ "discussion").writeNullable[Array[String]] and
  (JsPath \ "created").writeNullable[DateTime] and
  (JsPath \ "updated").writeNullable[DateTime]
)(unlift(Plan.unapply))

  implicit val userFormatFromJSON =Json.fromJson[Plan]
  implicit val userFormatToJSON =Json.toJson[Plan]
  /*implicit val planFormat = Json.format[Plan]*/

  implicit val messageFormat = Json.format[Message]
  import play.api.libs.json.Json

  implicit val comparator:Comparator[(String, List[Plan])] with Object {def compare(o1: (String, List[Plan]), o2: (String, List[Plan])): Int} = new Comparator[(String, List[Plan])] {
    override def compare(o1: (String, List[Plan]), o2: (String, List[Plan])): Int = {
      o2._2.length - o1._2.length
    }
  }
  implicit val order = Ordering.comparatorToOrdering[(String, List[Plan])]

  implicit val storeAndPlansWriter: Writes[(String,List[Plan])] = new Writes[(String,List[Plan])] {
    def writes(storesAndPlanObj: (String,List[Plan])): JsValue = Json.obj("store"->storesAndPlanObj._1,"plans"->storesAndPlanObj._2)
  }
}

