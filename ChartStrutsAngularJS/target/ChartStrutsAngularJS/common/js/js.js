let app = angular.module('exelApp', []);

app.controller('chartCtrl', function ($scope, $rootScope, $http) {
    $scope.getFullChart = function () {
        $http.get("http://localhost:4200/getExelData")
            .then(function (res) {
                $scope.data = res.data;
                $scope.data.seriesData.forEach(function(item){
                    item.visible = true;
                });
                let newOptions = setOptions($scope.data,$http);
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

    $scope.toggleExelData = function (id,visible) {
        let data = { id: id,visible: visible }
        $rootScope.$broadcast("emitHideData", data);
    }

})

function setOptions(data,$http) {
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
                console.log(this.columnIndex)
                console.log(event.point.x)

                let indexOfSheet = this.columnIndex;
                let indexOfColumn = event.point.x;
                $http.get(`http://localhost:4200/getDataByMin/${indexOfSheet}/${indexOfColumn}`)
                    .then(function(res) {
                        console.log(res.data);
                        let newOptions2 = setOptions2(res.data);
                        let chart2 = new Highcharts.chart(newOptions2);
                    })
            }
        }
    }
    return options;
}

function setOptions2(data) {
    let options = opts2;
    options.title.text = data.chartTitle;
    // options.xAxis.categories = data.chartXAxisCate;
    options.yAxis.title.text = data.chartYAxisTitleText;
    options.series = data.seriesDataByHour[0][0];

    return options;
}