
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
    deleteUser: (@id,@rowIndex) ->
             @$log.debug "updateUser()"
             @UserService.deleteUser(@id)
             .then(
                 (data) =>
                   @$log.debug "Promise returned #{data} User"
                   @user = data
                   ##Need to update message
                   @users.splice(rowIndex, 1)
                   @$log.info("User deleted ")
               ,
               (error) =>
                   @$log.error "Unable to soft-delete User: #{error}"
             )
controllersModule.controller('UserController', ['$log', 'UserService', UserController])