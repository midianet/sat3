'use strict';

angular.module('myApp.alunosFormPadrao', ['ngRoute'])
    .controller('AlunosFormPadraoCtrl', ['$scope', '$http', 'acessos', 'Messages', 'Validation', 'Notifica', '$routeParams', '$controller', function ($scope, $http, acessos, Messages, Validation, Notifica, $routeParams, $controller) {

        angular.extend(this, $controller('BaseController', {$scope: $scope}));

        $scope.Aluno = function () {
            return {
                id: null,
                nome: "",
                telefone: ""
            }
        };

        $scope.aluno = $scope.Aluno();

        // process the form
        $scope.limparForm = function () {
            $scope.aluno = $scope.Aluno();
            Validation.limpar('aluno-form', false);
            $scope.isUpdate = false;
        };

        //metodo opcional
        $scope.erroAoSalvar = function(rejection) {
            //faz alguma coisa
            console.log("Erro ao Salvar:");
            console.log(rejection);

            if (rejection != null) {
                Validation.exibir('aluno-form', rejection.validations, null, false);
            } else {
                Notifica.exibaErro(null, Messages.erroEfetuarOp);
            }
        }

        //metodo opcional
        $scope.sucessoAoSalvar = function(data) {
            //faz alguma coisa
            console.log("Sucesso ao Salvar:");
            console.log(data);
        }



        $scope.edit = function (id) {

            if (id && $scope.aluno.id != id) {

                $scope.limparForm();
                $scope.isUpdate = true;

                $http({
                    method: 'GET',
                    url: '/sat2-app/api/aluno/' + id
                }).success(function (aluno) {
                    if (aluno.nascimento){
                        aluno.nascimento = new Date(aluno.nascimento);
                    }
                    $scope.aluno = aluno;
                }).error(function (data) {
                    $scope.limparForm();
                    Notifica.exibaErro(null, Messages.erroEfetuarOp);
                });

            }
        };


        $scope.delete = function () {

            $scope.limparForm();

            if ($scope.aluno != null) {
                $http({
                    method: 'DELETE',
                    url: '/sat2-app/api/aluno/' + $scope.aluno.id
                }).success(function (response) {
                    $("#confirm-delete").modal('hide');
                    $('#dataTables-alunos').DataTable().ajax.reload(null, false);
                    $scope.aluno = $scope.Aluno();
                    Notifica.exibaSucesso(null, Messages.registroRemovido);
                }).error(function (response) {
                    Notifica.exibaErro(null, Messages.erroEfetuarOp);
                });
            }
        };


        $scope.edit($routeParams.id);

    }]);



