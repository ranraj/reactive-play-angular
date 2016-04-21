package helper

import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.bson.{BSONValue, BSONObjectID}
/*implicit*/
import play.api.Play.current

/**
 * Created by ranjithrajd on 20/4/16.
 */

trait MongoHelper extends ContextHelper{
  lazy val db = ReactiveMongoPlugin.db
}
object MongoHelper extends MongoHelper {

  def identify(bson: BSONValue) = bson.asInstanceOf[BSONObjectID].stringify

}