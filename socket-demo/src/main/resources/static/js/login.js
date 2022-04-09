$(function () {
    $('#login').click(function () {
        let username = $("#username").val();
        let password = $("#password").val();
        if (username === '' || password === '') {
            commonUtil.message("输入不能为空", "info");
            return;
        }
        let msg = $("#loginMsg");

        $.ajax({
            url: "/user/login",
            data: $("#form_login").serialize(),
            type: "POST",
            dataType: "json",
            success: function (data) {
                // console.log(data);
                if (data.code === "200") {
                    window.location.href = "/index";
                } else {
                    msg.css('display', "block");
                    msg.html('' + data.message);
                }
            }, error: function () {
                msg.css('display', "block");
                msg.html('' + "服务器无响应");
            }
        })
    })

});
