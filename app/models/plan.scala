
package models

import java.util.Comparator

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTimeZone, DateTime}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import json.MongoImplicits._
case class Plan( override var _id : Option[BSONObjectID],
                 title: Option[String],
                 content: String,
                 store:List[String],
                 active:Boolean,
                 override var created: Option[DateTime] =None,
                 override var updated: Option[DateTime] = None) extends TemporalModel {
}
object Plan {
  implicit val format = Json.format[Plan]
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

