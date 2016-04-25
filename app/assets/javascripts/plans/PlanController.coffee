
class PlanController

    constructor: (@$log, @PlanService,@$mdToast) ->
        @$log.debug "constructing PlanController"
        @plans = []
        @getAllPlans()
        @plan = {}
        @plansGroupByHash =[]
        @plansByHash = {}
        @hideRecentDisplay = false

    getAllPlans: () ->
        @$log.debug "getAllPlans()"
        @hideRecentDisplay=false
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
             @PlanService.deletePlan(@id.id)
             .then(
                 (data) =>
                   @$log.debug "Promise returned #{data} Plan"
                   @plan = data
                   ##Need to update message
                   @plans.splice(rowIndex, 1)
                   @toast('Plan deleted.')
                   @$log.info("Plan deleted ")
               ,
               (error) =>
                   @$log.error "Unable to soft-delete Plan: #{error}"
             )
    toast:(message) ->
        @$mdToast.show(
                                 @$mdToast.simple()
                                   .textContent(message)
                                   .position('bottom left' )
                                   .hideDelay(3000)
                               );
    viewPlanDetails : (@selectedPlan) ->
            @$log.debug "viewPlanDetails for #{@selectedPlan}"
            @plan = @selectedPlan

    saveOrUpdate: () ->
          @$log.debug "saveOrUpdate #{@plan}"
          if(@plan.id)
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
                @toast('Plan created.')
                @getAllPlans()
              ,
              (error) =>
                @$log.error "Unable to create Plan: #{error}"
              )

    updatePlan: () ->
          @$log.debug "updatePlan()"
          @plan.active = true
          @PlanService.updatePlan(@plan.id,@plan)
          .then(
              (data) =>
                @$log.debug "Promise returned #{data} Plan"
                @clear()
                @toast('Plan updated.')
                @getAllPlans()
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

    getPlansGroupByHash: () ->
            @$log.debug "controller getPlansGroupByHash()"
            @PlanService.listPlansGroupByHash()
            .then(
                (data) =>
                    @$log.debug "Promise returned #{data.length} Hash Lists Plans"
                    @plansGroupByHash = data
                ,
                (error) =>
                    @$log.error "Unable to get Plans: #{error}"
                )

    findPlansByHash: (hashId) ->
            @$log.debug "controller findPlansByHash(#{hashId})"
            @hideRecentDisplay=true
            @PlanService.listPlansByHash(hashId)
            .then(
                (data) =>
                    @$log.debug "Promise returned #{data.length} Hash Lists Plans"
                    @plansByHash = data
                ,
                (error) =>
                    @$log.error "Unable to get Plans: #{error}"
                )
controllersModule.controller('PlanController', ['$log', 'PlanService','$mdToast', PlanController])