package dao

import exceptions._
import helper.MongoHelper
import org.slf4j.{LoggerFactory, Logger}
import reactivemongo.core.commands.LastError
import reactivemongo.core.errors.DatabaseException
import scala.concurrent.Future
/**
 * Created by ranjithrajd on 20/4/16.
 */


trait BaseDAO extends MongoHelper{
  private final val logger: Logger = LoggerFactory.getLogger(classOf[BaseDAO])
  val collectionName: String

  def ensureIndexes: Future[List[Boolean]]

  def Recover[S](operation: Future[LastError])(success: => S): Future[Either[ServiceException, S]] = {
    operation.map {
      lastError => lastError.inError match {
        case true => {
          logger.error(s"DB operation did not perform successfully: [lastError=$lastError]")
          Left(DBServiceException(lastError))
        }
        case false => {
          Right(success)
        }
      }
    } recover {
      case exception =>
        logger.error(s"DB operation failed: [message=${exception.getMessage}]")

        // TODO: better failure handling here
        val handling: Option[Either[ServiceException, S]] = exception match {
          case e: DatabaseException => {
            e.code.map(code => {
              logger.error(s"DatabaseException: [code=${code}, isNotAPrimaryError=${e.isNotAPrimaryError}]")
              code match {
                case 10148 => {
                  Left(OperationNotAllowedException("", nestedException = e))
                }
                case 11000 => {
                  Left(DuplicateResourceException(nestedException = e))
                }
              }
            })
          }
        }
        handling.getOrElse(Left(UnexpectedServiceException(exception.getMessage, nestedException = exception)))
    }
  }
}
