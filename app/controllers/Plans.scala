package controllers

import javax.inject.Singleton

import com.fasterxml.jackson.annotation.JsonValue
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor
import services.PlansService

import scala.collection.immutable.ListMap
import scala.concurrent.Future

/**
 * The Plans controllers encapsulates the Rest endpoints and the interaction with the MongoDB, via ReactiveMongo
 * play plugin. This provides a non-blocking driver for mongoDB as well as some useful additions for handling JSon.
 * @see https://github.com/ReactiveMongo/Play-ReactiveMongo
 */
@Singleton
class Plans extends Controller with MongoController {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Plans])

  /*
   * Get a JSONCollection (a Collection implementation that is designed to work
   * with JsObject, Reads and Writes.)
   * Note that the `collection` is not a `val`, but a `def`. We do _not_ store
   * the collection reference to avoid potential problems in development with
   * Play hot-reloading.
   */
  def collection: JSONCollection = db.collection[JSONCollection]("plans")

  // ------------------------------------------ //
  // Using case classes + Json Writes and Reads //
  // ------------------------------------------ //

  import models.PlanJsonFormats._
  import models._

  def createPlan = Action.async(parse.json) {
    request =>
    /*
     * request.body is a JsValue.
     * There is an implicit Writes that turns this JsValue as a JsObject,
     * so you can call insert() with this JsValue.
     * (insert() takes a JsObject as parameter, or anything that can be
     * turned into a JsObject using a Writes.)
     */
      request.body.validate[Plan].map {
        plan =>
        // `plan` is an instance of the case class `models.Plan`
          collection.insert(plan).map {
            lastError =>
              logger.debug(s"Successfully inserted with LastError: $lastError")
              Created(s"Plan Added")
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def updatePlan(id: String) = Action.async(parse.json) {
    request =>
      request.body.validate[Plan].map {
        plan =>
          // find our plan by first name and last name
          val nameSelector = Json.obj("_id" -> Json.obj("$oid"->id))
          collection.update(nameSelector, plan).map {
            lastError =>
              logger.debug(s"Successfully updated with LastError: $lastError")
              Created(s"Plan Changed")
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }
  def softDeletePlan(id: String) = Action.async {
    request =>
      implicit  val objectId = id;
      // find our plan by first name and last name
      val nameSelector = Json.obj("_id" -> Json.obj("$oid"->id))
      collection.update(nameSelector, Json.obj("$set"->Json.obj("active"->"false"))).map {
        lastError =>
          logger.debug(s"Successfully deleted with LastError: $lastError")
          Ok(s"Plan deleted")
      }
  }

  def rawDeletePlan(id: String) = Action.async {
    collection.remove(Json.obj("_id" -> Json.obj("$oid"->id))).map(_ => Ok)
  }

  def findPlan(id: String) = Action.async {

    val futurePlans: Future[Option[Plan]] = collection.
      find(Json.obj("_id" -> Json.obj("$oid"->id),"active" -> true)).one[Plan]

    futurePlans.map{
        case plan:Some[Plan] => Ok(Json.toJson(plan))
        case None => NoContent
    }
  }
  def fetchPlansFromService:Future[List[Plan]] = {
    // let's do our query
    val cursor: Cursor[Plan] = collection.
      // find all
      find(Json.obj("active" -> true)).
      // sort them by creation date
      sort(Json.obj("created" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[Plan]

    // collect the JsonObjects in List
    val futurePlansList: Future[List[Plan]] = cursor.collect[List]()
    futurePlansList
  }
  def findPlans = Action.async {
    val futurePersonsJsonArray: Future[JsArray] = fetchPlansFromService.map { plans =>
      Json.arr(plans)
    }
    // map as json array
    futurePersonsJsonArray.map {
      plans =>
        Ok(plans(0))
    }
  }
  def findAllStores = Action.async{
    val futureStoreList = fetchPlansFromService.map { plans => {
      for {
        plan <- plans
        stores <- plan.store
      } yield (stores)
    }
    }

    val futureStoreArray = futureStoreList.map{ stores =>
    Json.arr(stores.distinct)
    }
    futureStoreArray.map {
      plans =>
        Ok(plans(0))
    }
  }
  def findPlansByHash = Action.async{
    val futureTupleList = fetchPlansFromService.map{ plans => {
      val storeToPlan:List[(String,Plan)] = for{
        plan <- plans
        stores <- plan.store
      }yield (stores,plan)
        val plansByTag = storeToPlan.groupBy{
          case(store,plan)=> store
        }.mapValues{
          storeToPlanList=>storeToPlanList.map{storeAndPlan => storeAndPlan._2}}
      plansByTag
      }
    }

    val futurePersonsJsonArray: Future[JsArray] = futureTupleList.map { plans =>
      Json.arr(plans)
    }

    futurePersonsJsonArray.map {
      plans =>
        Ok(plans(0))
    }
  }

}
