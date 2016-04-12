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


controllersModule.controller('PlanDetailsController', ['$log', '$location', '$routeParams', 'PlanService', PlanDetailsController])