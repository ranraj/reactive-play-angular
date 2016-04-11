class PlanDetailsController

  constructor: (@$log, @$location, @$routeParams, @PlanService) ->
      @$log.debug "constructing PlanDetailsController"
      @plan = {}
      @addOrEdit()


  addOrEdit: () ->
      id = @$routeParams.id
      if(id)
        @mode = 1
        @findPlan(id)
      else
        @mode = 0

  saveOrUpdate: () ->
      debugger;
      @$log.debug "mode #{@mode}"
      if(@mode == 1)
        @updatePlan()
      else
        @createPlan()

  createPlan: () ->
      @$log.debug "createPlan()"
      @plan.active = true
      @PlanService.createPlan(@plan)
      .then(
          (data) =>
            @$log.debug "Promise returned #{data} Plan"
            @plan = data
            @$location.path("/plans")
          ,
          (error) =>
            @$log.error "Unable to create Plan: #{error}"
          )

  updatePlan: () ->
      @$log.debug "updatePlan()"
      @plan.active = true
      @PlanService.updatePlan(@$routeParams.id,@plan)
      .then(
          (data) =>
            @$log.debug "Promise returned #{data} Plan"
            @plan = data
            @$location.path("/plans")
        ,
        (error) =>
            @$log.error "Unable to update Plan: #{error}"
      )

  findPlan: (id) ->
      # route params must be same name as provided in routing url in app.coffee
        #id = @$routeParams.id
        @$log.debug "findPlan route params: #{id}"
        @PlanService.findPlan(id)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} Plan"
                # find a plan with the name of firstName and lastName
                # as filter returns an array, get the first object in it, and return it
                @plan = data[0]
        ,
            (error) =>
                @$log.error "Unable to get Plans: #{error}"
        )


controllersModule.controller('PlanDetailsController', ['$log', '$location', '$routeParams', 'PlanService', PlanDetailsController])