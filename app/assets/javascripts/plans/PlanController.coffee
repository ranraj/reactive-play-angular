
class PlanController

    constructor: (@$log, @PlanService) ->
        @$log.debug "constructing PlanController"
        @plans = []
        @getAllPlans()

    getAllPlans: () ->
        @$log.debug "getAllPlans()"

        @PlanService.listPlans()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Plans"
                @plans = data
            ,
            (error) =>
                @$log.error "Unable to get Plans: #{error}"
            )
    deletePlan: (@id,@rowIndex) ->
             @$log.debug "updatePlan()"
             @PlanService.deletePlan(@id)
             .then(
                 (data) =>
                   @$log.debug "Promise returned #{data} Plan"
                   @plan = data
                   ##Need to update message
                   @plans.splice(rowIndex, 1)
                   @$log.info("Plan deleted ")
               ,
               (error) =>
                   @$log.error "Unable to soft-delete Plan: #{error}"
             )
controllersModule.controller('PlanController', ['$log', 'PlanService', PlanController])