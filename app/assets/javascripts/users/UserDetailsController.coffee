class UserDetailsController

  constructor: (@$log, @$location, @$routeParams, @UserService) ->
      @$log.debug "constructing UserDetailsController"
      @user = {}
      @addOrEdit()


  addOrEdit: () ->
      id = @$routeParams.id
      if(id)
        @mode = 1
        @findUser(id)
      else
        @mode = 0

  saveOrUpdate: () ->
      debugger;
      @$log.debug "mode #{@mode}"
      if(@mode == 1)
        @updateUser()
      else
        @createUser()

  createUser: () ->
      @$log.debug "createUser()"
      @user.active = true
      @UserService.createUser(@user)
      .then(
          (data) =>
            @$log.debug "Promise returned #{data} User"
            @user = data
            @$location.path("/users")
          ,
          (error) =>
            @$log.error "Unable to create User: #{error}"
          )

  updateUser: () ->
      @$log.debug "updateUser()"
      @user.active = true
      @UserService.updateUser(@$routeParams.id,@user)
      .then(
          (data) =>
            @$log.debug "Promise returned #{data} User"
            @user = data
            @$location.path("/users")
        ,
        (error) =>
            @$log.error "Unable to update User: #{error}"
      )

  findUser: (id) ->
      # route params must be same name as provided in routing url in app.coffee
        #id = @$routeParams.id
        @$log.debug "findUser route params: #{id}"
        @UserService.findUser(id)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} User"
                # find a user with the name of firstName and lastName
                # as filter returns an array, get the first object in it, and return it
                @user = data
        ,
            (error) =>
                @$log.error "Unable to get Users: #{error}"
        )


controllersModule.controller('UserDetailsController', ['$log', '$location', '$routeParams', 'UserService', UserDetailsController])