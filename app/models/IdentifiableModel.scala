package models

import reactivemongo.bson.BSONObjectID

/**
 * Created by ranjithrajd on 20/4/16.
 */
trait IdentifiableModel {
  var _id: Option[BSONObjectID]

  def identify = _id.map(value => value.stringify).getOrElse("")
}
