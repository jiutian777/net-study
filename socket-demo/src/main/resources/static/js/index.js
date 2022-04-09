$(function () {
    /*
    ** 不同页面切换转场效果
    ** $.mobile.changePage ('/test.html', 'slide/pop/fade/slideup/slidedown/flip/none', false, false);
    */
    $('.list-group-item,.menu a').click(function () {
        $.mobile.changePage($(this).attr('href'), {
            transition: 'flip', //转场效果
            reverse: true       //默认为false,设置为true时将导致一个反方向的转场
        });
    });

    //初始化fileinput
    var FileInput = function () {
        var oFile = {};
        //初始化fileinput控件（第一次初始化）
        oFile.Init = function (ctrlName, uploadUrl) {
            var control = $('#' + ctrlName);
            //初始化上传控件的样式
            control.fileinput({
                language: 'zh', //设置语言
                uploadUrl: uploadUrl, //上传的地址
                allowedFileExtensions: ['jpg', 'gif', 'png'],//接收的文件后缀
                uploadAsync: true, //默认异步上传
                showUpload: true, //是否显示上传按钮
                showCaption: false,//是否显示标题
                browseClass: "btn btn-primary", //按钮样式
                //dropZoneEnabled: false,//是否显示拖拽区域
                //minImageWidth: 50, //图片的最小宽度
                //minImageHeight: 50,//图片的最小高度
                //maxImageWidth: 1000,//图片的最大宽度
                //maxImageHeight: 1000,//图片的最大高度
                maxFileSize: 2048,//单位为kb，如果为0表示不限制文件大小
                //minFileCount: 0,
                maxFileCount: 1, //表示允许同时上传的最大文件个数
                enctype: 'multipart/form-data',
                validateInitialCount: true,
                previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
                msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
                uploadExtraData: function () {
                    var fileName = $("#img_upload").val();	//上传的本地文件绝对路径
                    var suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length);//后缀名
                    return {"suffix": suffix};
                }, //上传文件时额外传递的参数设置
                layoutTemplates: {
                    //actionDelete:'',  //去除上传预览缩略图中的删除图标
                    actionUpload: '', //去除上传预览缩略图中的上传图标；
                    //actionZoom:''   //去除上传预览缩略图中的查看详情预览的缩略图标。
                }
            }).on('filepreupload', function (event, data, previewId, index) {     //上传前
                // console.log("77777");
            }).on("filebatchselected", function (event, files) { //选择文件后处理事件
                // console.log("55555");
                // $(this).fileinput("upload"); //文件上传方法
            }).on('fileerror', function (event, data, msg) {  //文件上传失败
                setTimeout(function () {
                    commonUtil.message("文件上传失败！", "info");
                }, 1000);
            }).on("fileuploaded", function (event, data, previewId, index) {  //一个文件上传完成
                if (data.response.code === 200) {
                    qiNiuUpload(data.response);
                } else {
                    commonUtil.message(data.response.message, "info");                  //获取凭证失败
                }
            })
        }
        return oFile;
    };

    var qiNiuUpload = function (result) {
        let obj = $("#img_upload");
        let file = obj.get(0).files[0];
        let observer = {                         //设置上传过程的监听函数
            next(result) {                        //上传中(result参数带有total字段的 object，包含loaded、total、percent三个属性)
                Math.floor(result.total.percent);//查看进度[loaded:已上传大小(字节);total:本次上传总大小;percent:当前上传进度(0-100)]
            },
            error(err) {                          //失败后
                commonUtil.message(err.message, "info");
            },
            complete(res) {                       //成功后
                let userName = $('#userName').html();
                let newUserImg = result.domain + result.imgUrl;
                let formObject = {"userName": userName, "userImage": newUserImg};
                $.ajax({
                    url: "/user/update",
                    type: "POST",
                    contentType: "application/json;charset=utf-8",
                    data: JSON.stringify(formObject),
                    dataType: "json",
                    success: function (data) {
                        if (data.code === "200") {
                            // ?imageView2/2/h/100：展示缩略图，不加显示原图
                            $("#userImage").attr("src", newUserImg + "?imageView2/2/w/400/h/400/q/100");
                            setTimeout(function () {
                                commonUtil.message("文件上传成功！");
                                $("#myModal").modal("hide");
                            }, 1000);
                        } else {
                            commonUtil.message(data.message, "info");
                        }
                    }, error: function () {
                        commonUtil.message("服务器出错！", "warning");
                    }
                });
            }
        };
        let putExtra = {
            fname: "",                          //原文件名
            params: {},                         //用来放置自定义变量
            mimeType: null                      //限制上传文件类型
        };
        let config = {
            region: qiniu.region.z2,             //存储区域(z0:代表华东;z2:代表华南,不写默认自动识别)
            concurrentRequestLimit: 3            //分片上传的并发请求量
        };
        if (file === undefined || file === null) {
            return;
        }
        let observable = qiniu.upload(file, result.imgUrl, result.token, putExtra, config);
        let subscription = observable.subscribe(observer);          // 上传开始
        // 取消上传
        // subscription.unsubscribe();
    }

    function isJSON(str) {
        if (typeof str == 'string') {
            try {
                var obj = JSON.parse(str);
                return !!(typeof obj == 'object' && obj);
            } catch (e) {
                return false;
            }
        }
    }

    var websocket = null;

    function chat() {
        var ishttps = 'https:' === document.location.protocol;
        var host = window.location.host;
        var websocket_protocol;
        if (ishttps) {
            websocket_protocol = 'wss:';
        } else {
            websocket_protocol = 'ws:';
        }
        var urlPrefix = websocket_protocol + host.replace("http:", "")
                .replace("https:", "")
            + '/chat-room/';
        var username = $('#userName').html();
        var url = urlPrefix + username;
        websocket = new WebSocket(url);
        websocket.onopen = function () {
            console.log("建立 websocket 连接...");
        };
        websocket.onmessage = function (event) {
            // 服务端发送的消息
            var msg = event.data;
            console.log("msg:" + msg);
            if (isJSON(msg)) {
                var jsonObj = JSON.parse(msg);
                if (jsonObj.code === "403") {
                    alert(jsonObj.message);
                    window.location.href = "/toLogin";
                } else if (jsonObj.code === '200') {
                    let pp = null;
                    if (!isJSON(jsonObj.result)) {
                        pp = JSON.stringify(jsonObj.result);
                    }
                    if (pp == null || !isJSON(pp)) {
                        return;
                    }
                    var msgObj = JSON.parse(pp);
                    var senderName = msgObj.senderName;
                    var senderImg = msgObj.senderImg;
                    var senderMsg = msgObj.message;
                    var sendTime = msgObj.sendTime;
                    var msgType;
                    if (senderName === username) {
                        msgType = 'even';
                    } else {
                        msgType = 'odd';
                    }
                    let ss = ' <li class="' + msgType + '">\n' +
                        '                <a class="user" href="#"><img class="img-responsive avatar_ myImg" src="' + senderImg + '"\n' +
                        '                                              alt=""><span class="user-name">' + senderName + '</span></a>\n' +
                        '                <div class="reply-content-box">\n' +
                        '                    <span class="reply-time">' + sendTime + '</span>\n' +
                        '                    <div class="reply-content pr">\n' +
                        '                        <span class="arrow">&nbsp;</span>' + senderMsg + '\n' +
                        '                    </div>\n' +
                        '                </div>\n' +
                        '            </li>';
                    $('#message_content').append(ss);
                } else if (jsonObj.code === "210") {
                    let msg_tip = jsonObj.result;
                    if (msg_tip === '欢迎用户[ ' + username + ' ]来到聊天室') {
                        msg_tip = '欢迎您来到聊天室';
                    }
                    let ss = '<li style="color: #22DD48">' + msg_tip + '</li>';
                    $('#message_content').append(ss);
                }
                var h = $(document).height() - $(window).height();
                $("html,body").animate({scrollTop: h}, 500);
            }
        };
        websocket.onclose = function () {
            // $('#message_content').append('用户['+username+'] 已经离开聊天室!');
            console.log("关闭 websocket 连接...");
        };


        //客户端发送消息到服务器
        $('#sendMsg').click(function () {
            var msg = $('#message').val();
            if (msg === '') {
                commonUtil.message("输入不能为空", "info");
            } else {
                if (websocket && websocket.readyState === 1) {
                    websocket.send(msg);
                } else {
                    commonUtil.message("连接失败，请刷新页面", "info");
                }
                $('#message').val('');
            }
        });

        // $("#btn_send_point").click(function () {
        //     var sender = $("#in_sender").val();
        //     var receive = $("#in_receive").val();
        //     var message = $("#in_point_message").val();
        //     $.get("/chat-room/" + sender + "/to/" + receive + "?message=" + message, function () {
        //         alert("发送成功...")
        //     })
        // })
    }

    $('#show_sendMsg').click(function () {
        let b = $("#box_send_msg");
        if (b.css('display') === 'none') {
            b.css('display', 'block');
            var h = $(document).height() - $(window).height();
            $("html,body").animate({scrollTop: h}, 500);
        } else {
            b.css('display', 'none');
        }
    });

    $('#cancelSend').click(function () {
        $('#message').val('');
        $("#box_send_msg").css('display', 'none');
    });

    var fn_logout = function () {
        $.ajax({
            type: 'GET',
            url: "/logout",
            success: function (data) {
                // console.log(data);
                if (data.code === '200') {
                    window.location.href = '/toLogin';
                    if (websocket) {
                        websocket.close();
                    }
                }
            }, error: function () {
                commonUtil.message("服务器未响应", "info");
            }
        });
    };

    // 退出聊天室
    $('#logout').click(function () {
        commonUtil.message("正在退出....", "info", 800);
        setTimeout(function () {
            fn_logout();
        }, 800);
    });

    $.ajax({
        url: "/user/getByUserName",
        type: "POST",
        success: function (data) {
            if (data.code === "200") {
                let userJson = JSON.parse(data.result);
                $('#userName').html(userJson.userName);
                $('#userImage').attr('src', userJson.userImage);
                if ("WebSocket" in window) {
                    chat();
                } else {
                    commonUtil.message("您的浏览器不支持 WebSocket!", "info");
                }
            } else {
                commonUtil.message(data.message, "info");
            }
        }, error: function () {
            commonUtil.message("服务器未响应", "info");
        }
    });

    //弹出添加图片模态
    $("#uploadImg").click(function () {
        $("#myModal").modal("show");
    });
    var oFileInput = new FileInput();
    oFileInput.Init("img_upload", "/qiNiuUpToken");

    $('#baseSetting').click(function () {
        commonUtil.message("暂未开放，敬请期待！", "info");
    });

    var regPasswordSpecial = /[~!@#%&=;':",./<>_\}\]\-\$\(\)\*\+\.\[\?\\\^\{\|]/;
    var regPasswordAlpha = /[a-zA-Z]/;
    var regPasswordNum = /[0-9]/;

    // 密码匹配
    // 匹配字母、数字、特殊字符至少两种的函数
    function atLeastTwo(password) {
        var a = regPasswordSpecial.test(password) ? 1 : 0;
        var b = regPasswordAlpha.test(password) ? 1 : 0;
        var c = regPasswordNum.test(password) ? 1 : 0;
        return a + b + c;
    }

    $('#updatePwd').click(function () {
        $('#searchPwd-window').modal("show");
    });

    $('#resetPwd').click(function () {
        let new_pwd = $('#new_password').val();
        let again_pwd = $('#again_password').val();
        if (new_pwd === '' || again_pwd === '') {
            commonUtil.message("输入不能为空！", "info");
        } else if (again_pwd !== new_pwd) {
            commonUtil.message("前后密码不一致！", "info");
        } else if (again_pwd.length < 6) {
            commonUtil.message("密码太短，不能少于6个字符", "info");
        } else if (atLeastTwo(again_pwd) < 2) {
            commonUtil.message("密码中至少包含字母、数字、特殊字符的两种", "info");
        } else {
            let userName = $('#userName').html();
            let formObject = {"userName": userName, "pwd": again_pwd};
            $.ajax({
                type: "POST",
                url: "/user/update",
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify(formObject),
                dataType: "json",
                success: function (data) {
                    if (data.code === '200') {
                        commonUtil.message("修改成功!请重新登录", "success", 800);
                        setTimeout(function () {
                            fn_logout();
                        }, 800);
                    } else {
                        commonUtil.message(data.message, "warning");
                    }
                }, error: function () {
                    commonUtil.message("修改失败！", "warning");
                }
            });
        }
    });
});


