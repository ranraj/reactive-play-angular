

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


directivesModule.directive 'dateFrmt', ->
  {
    restrict: 'A'
    require: 'ngModel'
    link: (scope, element, attr, ngModel) ->

      fromUser = (date) ->
        console.log('from-dateFrmtaa'+date)
        year = date.getFullYear()
        month = forceTwoDigits(date.getMonth())
        day = forceTwoDigits(date.getDate()+1)
        console.log(month+'-finlfrom-'+year+'--'+day)
        return new Date(year, month, day)


      toUser = (date) ->
        date = new Date(date)
        console.log('to-dateFrmt'+date)
        year = date.getFullYear()
        month = date.getMonth()
        day = date.getDate()
        console.log(month+'-finlaa-'+year+'--'+day)
        return new Date(year, month, day)

      forceTwoDigits = (val) ->
        if val < 10
          return "0#{val}"
        return val


      ngModel.$parsers.push fromUser
      ngModel.$formatters.push toUser
      return

  }
