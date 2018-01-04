let chart;
let opts = {
    chart: {
        renderTo: 'ch-container',
        type: 'column'
    },
    title: {},
    subtitle: {},
    tooltip: {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
        '<td style="padding:0"><b>{point.y:.1f} kW</b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    },
    plotOptions: {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    },
    xAxis: {
        crosshair: true
    },
    yAxis: {
        min: 0,
        title: {}
    },
    series: [{}]
};
let opts2 = {
    chart: {
        renderTo:'container2',
        type: 'line'
    },
    title: {
        text: ''
    },
    subtitle: {
        text: ''
    },
    xAxis: {
        categories: [],
        tickmarkPlacement: 'on',
        title: {
            enabled: false
        }
    },
    yAxis: {
        title: {
            text: ''
        },
        labels: {
            formatter: function () {
                return this.value;
            }
        }
    },
    tooltip: {
        split: true,
        valueSuffix: ' kW'
    },
    plotOptions: {
        area: {
            stacking: 'normal',
            lineColor: '#666666',
            lineWidth: 1,
            marker: {
                lineWidth: 1,
                lineColor: '#666666'
            }
        }
    },
    series: []
};
chart = new Highcharts.chart(opts);




