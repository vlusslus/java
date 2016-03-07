(function(angular){
    'use strict';

    var module = angular.module('candidatesCheck',[]);


    module.controller('CandidatesListCtrl',function($scope, candidateService,$log){
        candidateService.getAll().then(function (candidates) {
            $scope.candidates = candidates;
        });


        $scope.add = function(){
            if (angular.isDefined($scope.name) && angular.isDefined($scope.surname)){
                candidateService.add($scope.surname,$scope.name).then(function (result) {
                    $log.info(angular.toJson(result));
                    $scope.candidates.push(result);
                });
            }
        }
    });

    module.factory('candidateService',function($http, $q){
        return {
            getAll:function(){
                return $http.get('/candidate').then(function (result) {
                    return result.data;
                });
            },
            add:function(surname,name){
                var candidate = {
                    name: name,
                    surname: surname
                };
                return $http.post('/candidate',candidate)
                    .then(function (result) {
                        return result.data;
                    })
                    .catch(function (result) {
                        alert('Упс не удалось добавить кандидата!');
                        return $q.reject(result);
                    });
            }
        }
    });
})(angular);