package json

import models.Plan
import play.api.UnexpectedException
import play.api.http.Writeable
import play.api.libs.json._

/**
 * Created by ranjithrajd on 20/4/16.
 */
object JsonFormats {

  implicit val bsonObjectIDFormat = play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat

  /**
   * Writeable for [[play.api.mvc.Result]] content.
   * @param w
   * @param wjs
   * @tparam A
   * @return
   */

  implicit def writeable[A](implicit w: Writes[A], wjs: Writeable[JsValue]): Writeable[A] = {
    wjs.map(a => {
      Json.toJson(a) match {
        case json: JsObject => transform(json)
        case json: JsArray => Json.toJson(
          for {
            item <- json.value
          } yield transform(item)
        )
        case _ => throw UnexpectedException(Some("Unsupported JSON type for writing!"))
      }
    })
  }

  def transform(json: JsValue) = {
    json.transform(
      __.json.update(
        (__ \ 'id).json.copyFrom((__ \ '_id \ '$oid).json.pick)
      ) andThen (
        (__ \ '_id).json.prune
        )
    ).fold(
      error => json,
      success => success
    )
  }
}