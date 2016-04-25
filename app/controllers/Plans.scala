package controllers

import javax.inject.Singleton

import exceptions.UnexpectedServiceException
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import services.PlansService
import scala.concurrent.Future

@Singleton
class Plans extends Controller {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Plans])

  import models._
  import json.JsonFormats._
  import models.Plan._

  def createPlan = Action.async(parse.json) {
    request =>
      request.body.validate[Plan].map {
        plan =>
          PlansService.assembleHashAndTitle(plan)
          PlansService.insert(plan).map {
            result => result.fold(
              exception => throw UnexpectedServiceException("Unable to create plan: [data=plan]", exception),
              plan => {
                logger.info(s"Plan successfully created: [_id=$plan]")
                Ok(plan)
              })
          }
      }.getOrElse(Future.successful(BadRequest(Json.obj("error"->"invalid json"))))
  }

  def updatePlan(id: String) = Action.async(parse.json) {
    request =>
      request.body.validate[Plan].map {
        plan =>
          PlansService.update(id, plan).map {
            result =>
              result.fold(exception => throw UnexpectedServiceException("Unable to create plan: [data=plan]", exception),
                response => {
                  logger.info(s"Plan successfully updated: [_id=$response]")
                  Ok(Json.obj("result" -> "Deleted successfully"))
                })
          }
      }.getOrElse(Future.successful(BadRequest(Json.obj("error"->"invalid json"))))
  }

  def deActivatePlan(id: String) = Action.async {
    request =>
      PlansService.deActivate(id).map {
        result =>
          result.fold(exception => throw UnexpectedServiceException("Unable to deactivate plan: [data=plan]", exception),
            plan => {
              logger.info(s"Plan successfully deactivate: [_id=$plan]")
              Ok(Json.obj("result" -> "Deactivated successfully"))
            })
      }
  }

  def deletePlan(id: String) = Action.async {
    PlansService.delete(id: String).map {
      result =>
        result.fold(exception => throw UnexpectedServiceException("Unable to remove plan: [data=plan]", exception),
          plan => {
            logger.info(s"Plan successfully removed: [_id=$plan]")
            Ok(Json.obj("result" -> "Deleted successfully"))
          })
    }
  }

  def findPlan(id: String) = Action.async {
    PlansService.findById(id).map { result =>
      result match {
        case plan: Some[Plan] => Ok(Json.toJson(plan))
        case None => NoContent
      }
    }
  }

  def findPlans = Action.async {
    writeResponse(PlansService.findActivePlans.map(plans => Json.arr(plans)))
  }

  def findAllStores = Action.async {
    writeResponse(PlansService.findAllStore.map(f => Json.arr(f)))
  }

  def findPlansGroupByHash = Action.async {
    writeResponse(PlansService.findPlansGroupByHashTag.map(plans => plans.toList.sorted).map(plans => Json.arr(Json.toJson(plans.take(5)))))
  }

  def findPlansByHash(hashId: String) = Action.async {
    writeResponse(PlansService.findPlansGroupByHashTag.map(_.get(hashId.toUpperCase())).map(plans => Json.arr(plans)))
  }

  def addComment(id:String) = Action.async(parse.json) {
    request =>
      request.body.validate[Message].map {
        message =>
          PlansService.addComment(id,message.content).map {
            result => result.fold(
              exception => throw UnexpectedServiceException("Unable to add comment: [data=comment]", exception),
              response => {
                logger.info(s"Comment successfully added: [comment=$response]")
                Ok(Json.obj("result"->"Comment added "))
              })
          }
      }.getOrElse(Future.successful(BadRequest(Json.obj("error"->"invalid json"))))
  }
  def addToDiscussion(id:String) = Action.async(parse.json) {
    request =>
      request.body.validate[Message].map {
        message =>
          PlansService.addDiscussion(id,message.content).map {
            result => result.fold(
              exception => throw UnexpectedServiceException("Unable to add discussion: [data=discussion]", exception),
              response => {
                logger.info(s"Discussion successfully added: [discussion=$response]")
                Ok(Json.obj("result"->"Discussion added "))
              })
          }
      }.getOrElse(Future.successful(BadRequest(Json.obj("error"->"invalid json"))))
  }
  def writeResponse(futurePersonsJsonArray: Future[JsArray]) = {
    futurePersonsJsonArray.map(plans => Ok(plans(0)))
  }

}
