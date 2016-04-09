
package models

import reactivemongo.bson.BSONObjectID

case class Plan( _id : String = BSONObjectID.generate.toString(),
                 content: String,
                 store:String,
                 active:Boolean)

object PlanJsonFormats {
  import play.api.libs.json.{Json, Format}

  // Generates Writes and Reads for Feed and Plan thanks to Json Macros
  //implicit val planFormat = Json.format[Plan]
  implicit val mongoFormat: Format[Plan] = Json.format[Plan]
}