import $ from 'jquery'
const proHost = window.location.protocol + "//" + window.location.host;
const href = window.location.href.split("bpmnjs")[0];
const key = href.split(window.location.host)[1];
const publicurl = proHost + key;
const tools222 = {

    // 下载BPMN
    downLoad(bpmnModeler) {
        var downloadLink = $("#downloadBpmn222")
        bpmnModeler.saveXML({format:true}, function(err, xml){
            if (err) {
                return console.error("could not save bpmn", err);
            }

            tools222.setEncoded(downloadLink, "diagram.bpmn", err ? null : xml);
        });
    },

    setEncoded(link, name, data) {
        var encodedData = encodeURIComponent(data);
        if (data) {
            link.addClass('active').attr({
                'href': 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData,
                'download': name
            });
        } else {
            link.removeClass('active');
        }
    },

    // 部署BPMN
    saveBpmn(bpmnModeler) {
        bpmnModeler.saveXML({format:true}, function(err, xml){
            if (err) {
                return console.error("could not save bpmn", err);
            }
            console.log(xml);
            var param = {
                "stringBPMN": xml
            }
            $.ajax({
                url:publicurl+'processDefinition/addDeploymentByString',
                type:'POST',
                dataType: 'json',
                data: param,
                success: function (result) {
                    if (result.status == '0') {
                        alert("BPMN部署成功");
                    } else {
                        alert(result.msg());
                    }
                },
                error: function (err) {
                    alert(err);
                }
            })
        });
    },

    // 上传BPMN 并查看BMNB
    uploadBPMN(bpmnModeler) {
        // 获取上传的 文件
        var FileUpload222 = document.myForm222.uploadFile222.files[0];
        var fm = new FormData();
        fm.append("processFile", FileUpload222);

        $.ajax({
            url:publicurl+'processDefinition/upload',
            type:'POST',
            data: fm,
            async: false,
            contentType: false,
            processData: false,

            success: function (result) {
                if (result.status == '0') {
                    // result.obj 是文件名
                    var url = publicurl + 'bpmn/' + result.obj;
                    tools222.openBPMN_URL(bpmnModeler, url);
                debugger;
                } else {
                    alert(result.msg());
                }
            },
            error: function (err) {
                alert(err);
            }
        })
    },

    // 在线打开BPMN
    openBPMN_URL(bpmnModeler, url) {
        $.ajax(url, {dataType:"text"}).done(async function (xml) {
            try {
                await bpmnModeler.importXML(xml);
            } catch (err) {
                console.error(err);
            }
        })
    }
}

export default tools222