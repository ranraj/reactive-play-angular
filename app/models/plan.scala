
package models

import java.util.Comparator

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTimeZone, DateTime}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import json.MongoImplicits._
case class Plan(  _id : Option[BSONObjectID],
                 title: Option[String],
                 content: String,
                 store:List[String],
                 active:Boolean,
                 createdDate: Option[DateTime],
                 updatedDate: Option[DateTime])

object PlanJsonFormats {
  import play.api.libs.json.Json

  // Plan JSON Format implicit for REST
  implicit val planReads: Reads[Plan] = (
  (JsPath \ "_id").readNullable[BSONObjectID] and
  (JsPath \ "title").readNullable[String].map(_.getOrElse("In Urgent")).map(Some(_)) and
  (JsPath \ "content").read[String] and
  (JsPath \ "store").read[List[String]] and
  (JsPath \ "active").read[Boolean] and
  (JsPath \ "createdDate").readNullable[DateTime].map(_.getOrElse(new DateTime())).map(Some(_))  and
  (JsPath \ "updatedDate").readNullable[DateTime].map(_.getOrElse(new DateTime())).map(Some(_))
)(Plan.apply _)
  val planDateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm a")
  implicit val planWrites: Writes[Plan] = (
  (JsPath \ "_id").writeNullable[BSONObjectID]   and
  (JsPath \ "title").writeNullable[String] and
  (JsPath \ "content").write[String] and
  (JsPath \ "store").write[List[String]] and
  (JsPath \ "active").write[Boolean] and
  (JsPath \ "createdDate").writeNullable[DateTime] and
  (JsPath \ "updatedDate").writeNullable[DateTime]
)(unlift(Plan.unapply))
  implicit val planFormatFromJSON =Json.fromJson[Plan]
  implicit val planFormatToJSON =Json.toJson[Plan]

  // Group by plans with store
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
