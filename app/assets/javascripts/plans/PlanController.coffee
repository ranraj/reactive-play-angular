
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

controllersModule.controller('PlanController', ['$log', 'PlanService', PlanController])