package dao

import models.Plan
import reactivemongo.api.indexes.IndexType.Ascending

import scala.concurrent.Future

/**
 * Created by ranjithrajd on 19/4/16.
 */
object PlansDAO extends DocumentDAO[Plan]{

   /* def insert(plan:Plan)(implicit collection:JSONCollection) ={
      collection.insert(plan)
    }
    def update(id:String,plan:Plan)(implicit collection:JSONCollection)={
      collection.update(Json.obj("_id" -> Json.obj("$oid"->id)), plan)
    }
    def deActivate(id:String)(implicit collection:JSONCollection)={
      collection.update(Json.obj("_id" -> Json.obj("$oid"->id)), Json.obj("$set"->Json.obj("active"->"false")))
    }
    def find(id:String)(implicit  collection: JSONCollection):Future[Option[Plan]]={
      collection.find(Json.obj("_id" -> Json.obj("$oid"->id),"active" -> true)).one[Plan]
    }
    def fetchPlans()(implicit  collection: JSONCollection):Future[List[Plan]] = {
      collection.
        find(Json.obj("active" -> true)).
        sort(Json.obj("created" -> -1)).
        cursor[Plan].collect[List]()
    }
    def delete(id:String)(implicit collection: JSONCollection)={
      collection.remove(Json.obj("_id" -> Json.obj("$oid"->id)))
    }*/

  override val collectionName: String = "plans"

  override def ensureIndexes = {
    Future.sequence(
      List(
        ensureIndex(List("email" -> Ascending), unique = true)
      )
    )
  }
}

