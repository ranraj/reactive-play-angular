

directivesModule.directive('appVersion', ['version', (version) ->
        (scope, elm, attrs) ->
            elm.text(version)
    ])

directivesModule.directive 'splitArray', ->
  {
    restrict: 'A'
    require: 'ngModel'
    link: (scope, element, attr, ngModel) ->

      fromUser = (text) ->
        text.split ' '

      toUser = (array) ->
        console.log(array)
        if(array)
          array.join ' '
        else
          array = []

      ngModel.$parsers.push fromUser
      ngModel.$formatters.push toUser
      return

  }
