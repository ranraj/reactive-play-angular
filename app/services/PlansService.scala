package services

import dao.PlansDAO
import exceptions.ServiceException
import models.Plan
import org.joda.time.DateTime
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import reactivemongo.bson.{BSONDateTime, BSONObjectID}
import json.JsonFormats._
import scala.concurrent.Future


/**
 * Created by ranjithrajd on 12/4/16.
 */
object PlansService {

  def insert(plan:Plan)={
    PlansDAO.insert(plan)
  }
  def update(id:String,plan:Plan)={
    PlansDAO.update(id,plan)
  }
  def deActivate(id:String)={
    PlansDAO.update(id,Json.obj("$set"->Json.obj("active"->false)))
  }
  def findById(id:String)={
    PlansDAO.findById(id)
  }
  def fetchAllPlans:Future[List[Plan]] = {
    PlansDAO.find()
  }
  def delete(id:String)={
    PlansDAO.remove(id)
  }
  def findActivePlans:Future[List[Plan]] ={
    PlansDAO.find(Json.obj("active" -> true))
  }
  def findPlansGroupByHashTag={
    val futureTupleList = findActivePlans.map{ plans => {
      val storeToPlan:List[(String,Plan)] = for{
        plan <- plans
        stores <- plan.store
      }yield (stores,plan)
      val plansByTag = storeToPlan.groupBy{
        case(store,plan)=> store.toUpperCase()
      }.mapValues{
        storeToPlanList=>storeToPlanList.map{storeAndPlan => storeAndPlan._2}}
      plansByTag
    }
    }
    futureTupleList
  }
  def findAllStore={
    findActivePlans.map { plans => {
      for {
        plan <- plans
        stores <- plan.store
      } yield (stores)
    }.distinct
    }
  }
}
