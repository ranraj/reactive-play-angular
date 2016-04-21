/*
package dao

import exceptions.ServiceException
import helper.db.DBQueryBuilder

import scala.concurrent.Future

import play.api.Logger
import play.api.libs.iteratee.Enumerator
import play.api.libs.json.{Reads, Json, JsObject}

import reactivemongo.api.Cursor
import reactivemongo.api.gridfs.{DefaultFileToSave, FileToSave, ReadFile, GridFS}
import reactivemongo.bson.{BSONValue, BSONObjectID}

/* Implicits */
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import reactivemongo.api.gridfs.Implicits.DefaultReadFileReader

/**
 * Created by ranjithrajd on 20/4/16.
 */
trait FileDAO extends BaseDAO {

  lazy val gfs = GridFS(db, collectionName)

  def insert(enumerator: Enumerator[Array[Byte]], file: DefaultFileToSave): Future[ReadFile[BSONValue]] = {
    gfs.save(enumerator, file)
  }

  def find(query: JsObject = Json.obj()): Cursor[ReadFile[BSONValue]] = {
    Logger.debug(s"Finding files: [collection=$collectionName, query=$query]")
    gfs.find(query)
  }

  def findById(id: String): Future[Option[ReadFile[BSONValue]]] = find(DBQueryBuilder.id(id)).headOption

  def findOne(query: JsObject = Json.obj()): Future[Option[ReadFile[BSONValue]]] = {
    Logger.debug(s"Finding one file: [collection=$collectionName, query=$query]")
    gfs.find(query).headOption
  }

  def removeById(id: String): Future[Either[ServiceException, Boolean]] = {
    Recover(gfs.remove(BSONObjectID(id))) {
      true
    }
  }

  def enumerate(file: ReadFile[_ <: BSONValue]): Enumerator[Array[Byte]] = {
    gfs.enumerate(file)
  }

  override def ensureIndexes = {
    // Let's build an index on our gridfs chunks collection if none:
    gfs.ensureIndex.map {
      case status =>
        Logger.info(s"GridFS index: [collection=$collectionName, status=$status]")
        List(status)
    }
  }
}
*/
