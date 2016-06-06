var lastDataTimestamp;
var isSCready=true;
var updatingData=false;
var hasUpdateFailAlert = false;
var chart1;
var chart2;
var chart3;
var chart4;
var chart5;
function updateTableArea(typeData){
    if (typeData==null)
        return
    console.info(typeData);
    chart1.series[0].addPoint([typeData.timestamp, parseFloat(typeData.soiltemperature)], true, false);
    chart2.series[0].addPoint([typeData.timestamp, parseFloat(typeData.soilmoisture)], true, false);
    chart3.series[0].addPoint([typeData.timestamp, parseFloat(typeData.soilfertility)], true, false);
    chart4.series[0].addPoint([typeData.timestamp, parseFloat(typeData.indoortemperature)], true, false);
    chart5.series[0].addPoint([typeData.timestamp, parseFloat(typeData.indoormoisture)], true, false);
}

function getSCready() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "GetSensorConnectorAction",
        success: function(json) {
            if (!json.ready){
                isSCready = false;
                $.alert({
                    title: '错误',
                    content: '传感器设置尚未配置，请返回首页进行配置',
                    confirm: function () {
                        return;
                    }
                });
            }else {
                isSCready = true;
            }
            hasUpdateFailAlert= false;
        },
        error: function(json) {
            updatingData = false;
            if (!hasUpdateFailAlert) {
                hasUpdateFailAlert= true;
                $.alert({
                    title: '错误',
                    content: '连接服务器出错',
                    confirm: function () {
                        hasUpdateFailAlert= false;
                        return;
                    }
                });


            }
        }
    });
}

function getData(needAll) {
    if (updatingData)
        return;
    if (!isSCready)
        return;
    updatingData = true;
    $.ajax({
        type: "GET",
        dataType: "json",
        url: (needAll)?"GetDataAction?needAll=true":"GetDataAction",
        success: function(json) {
            updatingData = false;
            if (needAll){
                $(json.dataLists).each(function(index,element){
                    updateTableArea(element)
                });
            }else{
                if (json.lastTimestamp == lastDataTimestamp){
                    return;
                }
                updateTableArea(json.dataList);
            }
            lastDataTimestamp = json.lastTimestamp;
            hasUpdateFailAlert= false;
        },
        error: function(json) {
            updatingData = false;
            if (!hasUpdateFailAlert) {
                hasUpdateFailAlert= true;
                $.alert({
                    title: '错误',
                    content: '连接服务器出错',
                    confirm: function () {
                        hasUpdateFailAlert= false;
                        return;
                    }
                });


            }
        }
    });
}


$(document).ready(function(){
    $("#username").text("当前用户："+$.cookie("MEMBER_LOGIN").split(",")[0]);
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    chart1 = new Highcharts.Chart({
        chart: {
            renderTo: 'chart_spline1', //图表放置的容器，DIV
            defaultSeriesType: 'spline', //图表类型为曲线图
            zoomType: 'x' ,  //x轴方向可以缩放
            /*
            events: {
                load: function() {
                    var series = this.series[0];
                    //每隔5秒钟，图表更新一次，y数据值在0-100之间的随机数
                    setInterval(function() {
                            var x = (new Date()).getTime(), // 当前时间
                                y = Math.random()*100;
                            series.addPoint([x, y], true, false);
                    }, 1000)
                }
            }*/
        },
        plotOptions: {
            spline: {
                //lineWidth: 1.5,
                //fillOpacity: 0.1,
                marker: {
                    enabled: true,
                    //states: {
                    //    hover: {
                    //        enabled: true,
                    //        radius: 2
                    //    }
                    //}
                },
                //shadow: false
            }
        },
        title: {
            text: '土壤温度数据走势图'  //图表标题
        },
        xAxis: { //设置X轴
            type: 'datetime',  //X轴为日期时间类型
            tickPixelInterval: 150  //X轴标签间隔
        },
        yAxis: { //设置Y轴
            title: '',
            max: 100, //Y轴最大值
            min: 0  //Y轴最小值
        },
        tooltip: {//当鼠标悬置数据点时的提示框
            formatter: function() { //格式化提示信息
                return '土壤温度'+
                    Highcharts.dateFormat('%H:%M:%S', this.x) +' '+
                    Highcharts.numberFormat(this.y, 2);
            }
        },
        legend: {
            enabled: true  //设置图例
        },
        exporting: {
            enabled: true  //设置导出按钮
        },
        credits: {
            enabled: false,
            text: '', //设置LOGO区文字
            url: '' //设置LOGO链接地址
        },
        series: [
            {
                name: '土壤温度',
                data: []
            }
        ]
    });
    chart2 = new Highcharts.Chart({
        chart: {
            renderTo: 'chart_spline2', //图表放置的容器，DIV
            defaultSeriesType: 'spline', //图表类型为曲线图
            zoomType: 'x' ,  //x轴方向可以缩放
        },
        title: {
            text: '土壤湿度数据走势图'  //图表标题
        },
        xAxis: { //设置X轴
            type: 'datetime',  //X轴为日期时间类型
            tickPixelInterval: 150  //X轴标签间隔
        },
        yAxis: { //设置Y轴
            title: '',
            max: 100, //Y轴最大值
            min: 0  //Y轴最小值
        },
        tooltip: {//当鼠标悬置数据点时的提示框
            formatter: function() { //格式化提示信息
                return '土壤湿度'+
                    Highcharts.dateFormat('%H:%M:%S', this.x) +' '+
                    Highcharts.numberFormat(this.y, 2);
            }
        },
        legend: {
            enabled: true  //设置图例
        },
        exporting: {
            enabled: true  //设置导出按钮
        },
        credits: {
            enabled: false,
            text: '', //设置LOGO区文字
            url: '' //设置LOGO链接地址
        },
        series: [
            {
                name: '土壤湿度',
                data: []
            }
        ]
    });
    chart3 = new Highcharts.Chart({
        chart: {
            renderTo: 'chart_spline3', //图表放置的容器，DIV
            defaultSeriesType: 'spline', //图表类型为曲线图
            zoomType: 'x' ,  //x轴方向可以缩放
        },
        title: {
            text: '土壤肥力数据走势图'  //图表标题
        },
        xAxis: { //设置X轴
            type: 'datetime',  //X轴为日期时间类型
            tickPixelInterval: 150  //X轴标签间隔
        },
        yAxis: { //设置Y轴
            title: '',
            max: 100, //Y轴最大值
            min: 0  //Y轴最小值
        },
        tooltip: {//当鼠标悬置数据点时的提示框
            formatter: function() { //格式化提示信息
                return '土壤肥力'+
                    Highcharts.dateFormat('%H:%M:%S', this.x) +' '+
                    Highcharts.numberFormat(this.y, 2);
            }
        },
        legend: {
            enabled: true  //设置图例
        },
        exporting: {
            enabled: true  //设置导出按钮
        },
        credits: {
            enabled: false,
            text: '', //设置LOGO区文字
            url: '' //设置LOGO链接地址
        },
        series: [
            {
                name: '土壤肥力',
                data: []
            }
        ]
    });
    chart4 = new Highcharts.Chart({
        chart: {
            renderTo: 'chart_spline4', //图表放置的容器，DIV
            defaultSeriesType: 'spline', //图表类型为曲线图
            zoomType: 'x' ,  //x轴方向可以缩放
        },
        plotOptions: {
            spline: {
                //lineWidth: 1.5,
                //fillOpacity: 0.1,
                marker: {
                    enabled: true,
                    //states: {
                    //    hover: {
                    //        enabled: true,
                    //        radius: 2
                    //    }
                    //}
                },
                //shadow: false
            }
        },
        title: {
            text: '室内温度数据走势图'  //图表标题
        },
        xAxis: { //设置X轴
            type: 'datetime',  //X轴为日期时间类型
            tickPixelInterval: 150  //X轴标签间隔
        },
        yAxis: { //设置Y轴
            title: '',
            max: 100, //Y轴最大值
            min: 0  //Y轴最小值
        },
        tooltip: {//当鼠标悬置数据点时的提示框
            formatter: function() { //格式化提示信息
                return '室内温度'+
                    Highcharts.dateFormat('%H:%M:%S', this.x) +' '+
                    Highcharts.numberFormat(this.y, 2);
            }
        },
        legend: {
            enabled: true  //设置图例
        },
        exporting: {
            enabled: true  //设置导出按钮
        },
        credits: {
            enabled: false,
            text: '', //设置LOGO区文字
            url: '' //设置LOGO链接地址
        },
        series: [
            {
                name: '室内温度',
                data: []
            }
        ]
    });
    chart5 = new Highcharts.Chart({
        chart: {
            renderTo: 'chart_spline5', //图表放置的容器，DIV
            defaultSeriesType: 'spline', //图表类型为曲线图
            zoomType: 'x' ,  //x轴方向可以缩放
        },
        plotOptions: {
            spline: {
                //lineWidth: 1.5,
                //fillOpacity: 0.1,
                marker: {
                    enabled: true,
                    //states: {
                    //    hover: {
                    //        enabled: true,
                    //        radius: 2
                    //    }
                    //}
                },
                //shadow: false
            }
        },
        title: {
            text: '室内湿度数据走势图'  //图表标题
        },
        xAxis: { //设置X轴
            type: 'datetime',  //X轴为日期时间类型
            tickPixelInterval: 150  //X轴标签间隔
        },
        yAxis: { //设置Y轴
            title: '',
            max: 100, //Y轴最大值
            min: 0  //Y轴最小值
        },
        tooltip: {//当鼠标悬置数据点时的提示框
            formatter: function() { //格式化提示信息
                return '室内湿度'+
                    Highcharts.dateFormat('%H:%M:%S', this.x) +' '+
                    Highcharts.numberFormat(this.y, 2);
            }
        },
        legend: {
            enabled: true  //设置图例
        },
        exporting: {
            enabled: true  //设置导出按钮
        },
        credits: {
            enabled: false,
            text: '', //设置LOGO区文字
            url: '' //设置LOGO链接地址
        },
        series: [
            {
                name: '室内湿度',
                data: []
            }
        ]
    });
    //getSCready();
    getData();
    setInterval(getData, 1000);
});