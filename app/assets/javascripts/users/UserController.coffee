
class UserController

    constructor: (@$log, @UserService) ->
        @$log.debug "constructing UserController"
        @users = []
        @getAllUsers()

    getAllUsers: () ->
        @$log.debug "getAllUsers()"

        @UserService.listUsers()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Users"
                @users = data
            ,
            (error) =>
                @$log.error "Unable to get Users: #{error}"
            )

controllersModule.controller('UserController', ['$log', 'UserService', UserController])