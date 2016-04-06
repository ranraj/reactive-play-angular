class UpdatePlanController

  constructor: (@$log, @$location, @$routeParams, @PlanService) ->
      @$log.debug "constructing UpdatePlanController"
      @plan = {}
      @findPlan()

  updatePlan: () ->
      @$log.debug "updatePlan()"
      @plan.active = true
      @PlanService.updatePlan(@$routeParams.content, @$routeParams.store, @plan)
      .then(
          (data) =>
            @$log.debug "Promise returned #{data} Plan"
            @plan = data
            @$location.path("/")
        ,
        (error) =>
            @$log.error "Unable to update Plan: #{error}"
      )

  findPlan: () ->
      # route params must be same name as provided in routing url in app.coffee
      content = @$routeParams.content
      store = @$routeParams.store
      @$log.debug "findPlan route params: #{content} #{store}"

      @PlanService.listPlans()
      .then(
        (data) =>
          @$log.debug "Promise returned #{data.length} Plans"

          # find a plan with the name of content and store
          # as filter returns an array, get the first object in it, and return it
          @plan = (data.filter (plan) -> plan.content is content and plan.store is store)[0]
      ,
        (error) =>
          @$log.error "Unable to get Plans: #{error}"
      )

controllersModule.controller('UpdatePlanController', ['$log', '$location', '$routeParams', 'PlanService', UpdatePlanController])