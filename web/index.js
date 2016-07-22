var lastDataTimestamp;
var isSCready=false;
var updatingData=false;
var hasUpdateFailAlert = false;
function updateTableArea(json){
    var typeData = json.dataList;
    if (typeData==null)
        return
    $("#soiltemperature").text(typeData.soiltemperature);
    $("#soilmoisture").text(typeData.soilmoisture);
    $("#soilfertility").text(typeData.soilfertility);
    $("#indoortemperature").text(typeData.indoortemperature);
    $("#indoormoisture").text(typeData.indoormoisture);
}
function getSCready() {
    $("#scsettingdiv").hide();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "GetSensorConnectorAction",
        success: function(json) {
            if (!json.ready){
                isSCready = false;
                $("#scsettingdiv").show();
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

function getData() {
    if (updatingData)
        return;
    if (!isSCready)
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
    $("#username").text("当前用户："+$.cookie("MEMBER_LOGIN").split(",")[0]);
    getSCready();
    getData();
    setInterval(getData, 1000);
});

$("#sub").click(function(){
    $.ajax({
        cache: false,
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

$("#scclient").click(function(){
    $("#schostdiv").show();
    $("#scportdiv").show();
});

$("#scserver").click(function(){
    $("#schostdiv").hide();
    $("#scportdiv").show();
});

$("#sctest").click(function(){
    $("#schostdiv").hide();
    $("#scportdiv").hide();
});

$("#subscsetting").click(function(){
    $.ajax({
        cache: false,
        type: "POST",
        url:"SetSensorConnectorAction",
        data:$("#scsettingform").serialize(),
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
            $.alert({
                title: '成功',
                content: '数据已提交',
                confirm: function(json){
                    $("#scsettingdiv").hide();
                    isSCready = json.ready;
                }
            });
        }
    });
    return false;

});
/*
$("#btnstop").click(function(){
    $.confirm({
        title: '警告',
        content: '这将停止数据监控，是否继续？',
        confirmButton: '确定',
        cancelButton: '取消',
        confirm: function(){
            $.ajax({
                cache: true,
                type: "POST",
                url:"CleanVoteAction",
                data: {},
                async: false,
                error: function(request) {
                    $.alert({
                        title: '错误',
                        content: '清空失败',
                        confirm: function(){
                            return;
                        }
                    });
                },
                success: function(json) {
                    updateTableArea(json);
                }
            });
            $.ajax({
                cache: true,
                type: "POST",
                url:"CleanDanMuAction",
                data: {},
                async: false,
                error: function(request) {
                    $.alert({
                        title: '错误',
                        content: '清空失败',
                        confirm: function(){
                            return;
                        }
                    });
                },
                success: function(json) {
                    updateDanMuList(json,true);
                }
            });
        },
        cancel: function(){
            console.log('the user clicked cancel');
        }
    });

    return false;

});
*/