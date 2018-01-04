<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>High Chart</title>
    <link rel="stylesheet" href="common/lib/bootstrap/dist/css/bootstrap.css">
    <link rel="stylesheet" href="common/css/main.css">
    <script src="common/lib/highcharts/highcharts.js"></script>
    <script src="common/lib/highcharts/exporting.js"></script>
</head>

<body ng-app="exelApp">


<div class="container">
    <div class="row">
        <div ng-controller="chartCtrl" ng-init="getFullChart()">
        </div>
    </div>
    <div class="row">
        <div class="col-sm-2" ng-controller="btnCtrl">
            <div ng-if="data" ng-repeat="sd in data">
                <button id="btnOke" class="btn btn-primary" ng-click="toggleExelData(sd.id,sd.visible)">{{sd.name}}</button>
            </div>
        </div>

        <div class="col-sm-10">
            <div id="ch-container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
            <div id="container2" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
        </div>
    </div>
</div>


<script src="common/lib/jquery/dist/jquery.js"></script>
<script src="common/lib/angular/angular.js"></script>
<script src="common/js/default-chart.js"></script>
<script src="common/js/js.js"></script>
</body>
</html>
