
dependencies = [
    'ngRoute',
    'ngMaterial',
    'ui.bootstrap',
    'myApp.filters',
    'myApp.services',
    'myApp.controllers',
    'myApp.directives',
    'myApp.common',
    'myApp.routeConfig'
]

app = angular.module('myApp', dependencies)

angular.module('myApp.routeConfig', ['ngRoute'])
    .config(['$routeProvider', ($routeProvider) ->
        $routeProvider
            .when('/aboutus', {
                          templateUrl: '/assets/partials/home.html'
            })
            .when('/users', {
                templateUrl: '/assets/partials/user/view.html'
            })
            .when('/users/edit/:id', {
                templateUrl: '/assets/partials/user/details.html'
            })
            .when('/users/create', {
                templateUrl: '/assets/partials/user/details.html'
            })
            .when('/', {
                  templateUrl: '/assets/partials/plan/view.html'
             })
            .otherwise({redirectTo: '/'})])
         .config(['$locationProvider', ($locationProvider) ->
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        })])

@commonModule = angular.module('myApp.common', [])
@controllersModule = angular.module('myApp.controllers', [])
@servicesModule = angular.module('myApp.services', [])
@modelsModule = angular.module('myApp.models', [])
@directivesModule = angular.module('myApp.directives', [])
@filtersModule = angular.module('myApp.filters', [])