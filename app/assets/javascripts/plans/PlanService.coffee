
class PlanService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing PlanService"

    listPlans: () ->
        @$log.debug "listPlans()"
        deferred = @$q.defer()

        @$http.get("/rest/plans")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Plans - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Plans - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    createPlan: (plan) ->
        @$log.debug "createPlan #{angular.toJson(plan, true)}"
        deferred = @$q.defer()

        @$http.post('/rest/plan', plan)
        .success((data, status, headers) =>
                @$log.info("Successfully created Plan - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create plan - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    updatePlan: (firstName, lastName, plan) ->
      @$log.debug "updatePlan #{angular.toJson(plan, true)}"
      deferred = @$q.defer()

      @$http.put("/rest/plan/#{firstName}/#{lastName}", plan)
      .success((data, status, headers) =>
              @$log.info("Successfully updated Plan - status #{status}")
              deferred.resolve(data)
            )
      .error((data, status, header) =>
              @$log.error("Failed to update plan - status #{status}")
              deferred.reject(data)
            )
      deferred.promise

servicesModule.service('PlanService', ['$log', '$http', '$q', PlanService])