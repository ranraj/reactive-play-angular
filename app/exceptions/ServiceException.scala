package exceptions

/**
 * Created by ranjithrajd on 20/4/16.
 */
trait ServiceException extends Exception {
  val message: String
  val nestedException: Throwable
}