package dao

import models.Plan
import reactivemongo.api.indexes.IndexType.Ascending

import scala.concurrent.Future

/**
 * Created by ranjithrajd on 19/4/16.
 */
object PlansDAO extends DocumentDAO[Plan]{

  override val collectionName: String = "plans"
  override def ensureIndexes = {
    Future.sequence(
      List(
        ensureIndex(List("title" -> Ascending), unique = true)
      )
    )
  }
}

