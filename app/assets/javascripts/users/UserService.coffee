
class UserService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserService"

    listUsers: () ->
        @$log.debug "listUsers()"
        deferred = @$q.defer()

        @$http.get("/rest/users")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Users - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Users - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    findUser: (@id) ->
            @$log.debug "findUser()"
            deferred = @$q.defer()

            @$http.get("/rest/user/#{id}")
            .success((data, status, headers) =>
                    @$log.info("Successfully fetched User - status #{status}")
                    deferred.resolve(data)
                )
            .error((data, status, headers) =>
                    @$log.error("Failed to fetch User - status #{status}")
                    deferred.reject(data)
                )
            deferred.promise

    createUser: (user) ->
        @$log.debug "createUser #{angular.toJson(user, true)}"
        deferred = @$q.defer()

        @$http.post('/rest/user', user)
        .success((data, status, headers) =>
                @$log.info("Successfully created User - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create user - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    updateUser: (id, user) ->
      @$log.debug "updateUser #{angular.toJson(user, true)}"
      deferred = @$q.defer()

      @$http.put("/rest/user/#{id}", user)
      .success((data, status, headers) =>
              @$log.info("Successfully updated User - status #{status}")
              deferred.resolve(data)
            )
      .error((data, status, header) =>
              @$log.error("Failed to update user - status #{status}")
              deferred.reject(data)
            )
      deferred.promise

    deleteUser: (id) ->
          @$log.debug "updateUser #{angular.toJson(id,true)}"
          deferred = @$q.defer()

          @$http.delete("/rest/user/s/#{id}")
          .success((data, status, headers) =>
                  @$log.info("Successfully deleted User - status #{status}")
                  deferred.resolve(data)
                )
          .error((data, status, header) =>
                  @$log.error("Failed to delete user - status #{status}")
                  deferred.reject(data)
                )
          deferred.promise

servicesModule.service('UserService', ['$log', '$http', '$q', UserService])