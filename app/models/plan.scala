
package models

case class Plan(content: String,
                 store:String,
                 active:Boolean)

object PlanJsonFormats {
  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and Plan thanks to Json Macros
  implicit val planFormat = Json.format[Plan]
}