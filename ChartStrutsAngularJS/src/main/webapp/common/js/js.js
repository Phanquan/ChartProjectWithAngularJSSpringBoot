let app = angular.module('exelApp', []);

app.controller('chartCtrl', function ($scope, $rootScope, $http) {
    $scope.getFullChart = function () {
        $http.get("http://localhost:4200/getExelData")
            .then(function (res) {
                $scope.data = res.data;
                $scope.data.seriesData.forEach(function (item) {
                    item.visible = true;
                });
                let newOptions = setOptions($scope.data, $rootScope, $http);
                chart = new Highcharts.chart(newOptions);
                $rootScope.$broadcast("emitChartData", $scope.data.seriesData);
            })
    }

    $rootScope.$on("emitHideData", function (e, data) {
        data.visible
            ? chart.series[data.id].hide()
            : chart.series[data.id].show();
    })
})

app.controller('btnCtrl', function ($scope, $rootScope) {
    $scope.data = [];

    $rootScope.$on("emitChartData", function (e, data) {
        $scope.data = [...data];
    });

    $scope.toggleExelData = function (id, visible) {
        let data = { id: id, visible: visible }
        $rootScope.$broadcast("emitHideData", data);
    }

})

app.controller('chartbyMinCtrl', function ($scope, $rootScope, $http) {
    $scope.getChartByMinData = function () {
        $http.get("http://localhost:4200/getDataByMin")
            .then(function (res) {
                $scope.dataByMin = res.data;
            })

    }

    $rootScope.$on('emitPositionData', function (e, data) {
        $scope.indexObj = data;
        console.log($scope.indexObj);
        $scope.genChart2();
    })

    $scope.genChart2 = function () {
        console.log($scope.indexObj);
        if($scope.dataByMin){
            let newOptions2 = setOptions2($scope.dataByMin, $scope.indexObj);
            let chart2 = new Highcharts.chart(newOptions2);
        }
    }

})

function setOptions(data, $rootScope, $http) {
    let options = opts;
    options.title.text = data.chartTitle;
    options.subtitle.text = data.chartSubTitle;
    options.xAxis.categories = data.chartXAxisCate;
    options.yAxis.title.text = data.chartYAxisTitleText;
    options.series = data.seriesData;
    options.plotOptions.series = {
        cursor: 'pointer',
        events: {
            click: function (event) {
                let data = {
                    indexOfSheet: this.columnIndex,
                    indexOfColumn: event.point.x
                }
                $rootScope.$broadcast('emitPositionData', data);
            }
        }
    }
    return options;
}

function setOptions2(data, iObj) {
    let options = opts2;
    let seriesDataByMin;
    data.seriesDataByMin.forEach(function (eleOfSheet, indexOfSheet) {
        if (indexOfSheet === iObj.indexOfSheet) {
            eleOfSheet.forEach(function (eleOfColumn, indexOfColumn) {
                if (indexOfColumn === iObj.indexOfColumn) {
                    eleOfColumn[1].type = "line";
                    eleOfColumn[0].type = "area";
                    eleOfColumn[1].color = "#f49842";
                    options.series = eleOfColumn;
                    return;
                }
            })
            return;
        }
    })
    options.title.text = data.chartTitle;
    options.yAxis.title.text = data.chartYAxisTitleText;
    return options;
}