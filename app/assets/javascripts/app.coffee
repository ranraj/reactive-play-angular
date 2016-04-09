
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
            .when('/', {
                          templateUrl: '/assets/partials/home.html'
            })
            .when('/users', {
                templateUrl: '/assets/partials/user/view.html'
            })
            .when('/users/create', {
                templateUrl: '/assets/partials/user/create.html'
            })
            .when('/users/edit/:id', {
                templateUrl: '/assets/partials/user/update.html'
            })
            .when('/plans', {
                  templateUrl: '/assets/partials/plan/view.html'
             })
             .when('/plans/create', {
                   templateUrl: '/assets/partials/plan/create.html'
             })
             .when('/plans/edit/:content/:store', {
                   templateUrl: '/assets/partials/plan/update.html'
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