package models

import org.joda.time.DateTime

/**
 * Created by ranjithrajd on 20/4/16.
 */
trait TemporalModel extends IdentifiableModel {
  var created: Option[DateTime]
  var updated: Option[DateTime]
}