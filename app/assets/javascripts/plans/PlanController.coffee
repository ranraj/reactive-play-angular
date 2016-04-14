
class PlanController

    constructor: (@$log, @PlanService) ->
        @$log.debug "constructing PlanController"
        @plans = []
        @getAllPlans()
        @plan = {}
        @plansByHash =[]
        @getPlansByHash()

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
             @$log.debug "deletePlan()"
             @PlanService.deletePlan(@id._id.$oid)
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

    viewPlanDetails : (@selectedPlan) ->
            @$log.debug "viewPlanDetails for #{@selectedPlan}"
            @plan = @selectedPlan

    saveOrUpdate: () ->
          @$log.debug "saveOrUpdate #{@plan}"
          if(@plan._id)
            @updatePlan()
          else
            @createPlan()

    clear: () ->
          @$log.debug "clear #{@plan}"
          @plan = {}

    createPlan: () ->
          @$log.debug "createPlan()"
          @plan.active = true
          @PlanService.createPlan(@plan)
          .then(
              (data) =>
                @$log.debug "Promise returned #{data} Plan"
                @clear()
                @getAllPlans()
              ,
              (error) =>
                @$log.error "Unable to create Plan: #{error}"
              )

    updatePlan: () ->
          @$log.debug "updatePlan()"
          @plan.active = true
          @PlanService.updatePlan(@plan._id.$oid,@plan)
          .then(
              (data) =>
                @$log.debug "Promise returned #{data} Plan"
                @clear()
            ,
            (error) =>
                @$log.error "Unable to update Plan: #{error}"
          )

    findPlan: (id) ->
            #route params must be same name as provided in routing url in app.coffee
            #id = @$routeParams.id
            @$log.debug "findPlan #{id}"
            @PlanService.findPlan(id)
            .then(
                (data) =>
                    @$log.debug "Promise returned #{data} Plan"
                    # find a plan with the name of firstName and lastName
                    # as filter returns an array, get the first object in it, and return it
            ,
                (error) =>
                    @$log.error "Unable to get Plans: #{error}"
            )

    getPlansByHash: () ->
            @$log.debug "controller getPlansByHash()"

            @PlanService.listPlansByHash()
            .then(
                (data) =>
                    @$log.debug "Promise returned #{data.length} Hash Lists Plans"
                    @plansByHash = data
                ,
                (error) =>
                    @$log.error "Unable to get Plans: #{error}"
                )
controllersModule.controller('PlanController', ['$log', 'PlanService', PlanController])