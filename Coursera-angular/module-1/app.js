(function () {
  'use strict';

  angular
    .module('CheckItemsApp', [])
    .controller('CheckItemsController', CheckItemsController);

  /** @ngInject */
  function CheckItemsController($scope) {
    var vm = this;
    const MAX_ITEMS = 3;

    init();

    function init() {

      $scope.items = "";

    };

    $scope.checkNumberOfItems = function () {
      let items = $scope.items;

      if(items.length == 0) {
        $scope.resultMessage = "Input something first";
        return;
      }

      let splitted = items.split(",").map(e => {
        return e.trim();
      });

      let splittedAndFiletered = splitted.filter(e => {
        return e.length > 0;
      });

      if(splittedAndFiletered.length <= MAX_ITEMS) {
        $scope.resultMessage = "Enjoy!";
      } else {
        $scope.resultMessage = "Too much!";
      }

    };

  }

})();