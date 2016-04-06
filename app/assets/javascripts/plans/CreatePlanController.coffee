
class CreatePlanController

    constructor: (@$log, @$location,  @PlanService) ->
        @$log.debug "constructing CreatePlanController"
        @plan = {}

    createPlan: () ->
        @$log.debug "createPlan()"
        @plan.active = true
        @PlanService.createPlan(@plan)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} Plan"
                @plan = data
                @$location.path("/")
            ,
            (error) =>
                @$log.error "Unable to create Plan: #{error}"
            )

controllersModule.controller('CreatePlanController', ['$log', '$location', 'PlanService', CreatePlanController])