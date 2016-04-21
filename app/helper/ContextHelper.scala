package helper

import scala.concurrent.ExecutionContext
/**
 * Created by ranjithrajd on 20/4/16.
 */
trait ContextHelper {
  implicit def ec: ExecutionContext = ExecutionContext.Implicits.global
}
