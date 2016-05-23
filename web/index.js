var lastDataTimestamp;
var updatingData=false;
var hasUpdateFailAlert = false;
function updateTableArea(json){
    var typeData = json.dataList;
    $("#soiltemperature").text(typeData.soiltemperature);
    $("#soilmoisture").text(typeData.soilmoisture);
    $("#soilfertility").text(typeData.soilfertility);
    $("#indoortemperature").text(typeData.indoortemperature);
    $("#indoormoisture").text(typeData.indoormoisture);
}
function getData() {
    if (updatingData)
        return;
    updatingData = true;
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "GetDataAction",
        success: function(json) {
            updatingData = false;
            if (json.lastTimestamp == lastDataTimestamp){
                return;
            }
            updateTableArea(json);
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
    getData();
    setInterval(getData, 1000);
});

$("#sub").click(function(){
    $.ajax({
        cache: true,
        type: "POST",
        url:"SetDataAction",
        data:$("#datasetform").serialize(),
        async: false,
        error: function(request) {
            $.alert({
                title: '错误',
                content: '提交失败',
                confirm: function(){

                }
            });

        },
        success: function(json) {
            updateTableArea(json);
        }
    });
    return false;

});
