<%--
  Created by IntelliJ IDEA.
  User: author
  Date: 2017/9/15
  Time: 23:08
  To change this template use File | Settings | File Templates.
  1，获取RSA公钥
  2，对密码进行RSA加密
  3，发送注册请求
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户注册</title>
    <%@include file="common.jsp" %>
</head>
<body>
用户名：<input type="text" id="username"><span class="error"></span><br/>
密 码：<input type="password" id="password"><span class="error"></span><br/>
email：<input type="text" id="email"><span class="error"></span><br/>
<button type="submit" onclick="doRegister()">注册</button>
</body>
<script>
    var publicKey;
    var exponent;
    var modulus;
    $(function () {
        //发送ajax请求，获取publickey
        $.ajax({
            type: "GET",
            url: "<%=request.getContextPath()%>/user/getPubKey.do",
            dataType: "json",
            success: function (result) {
                if (result.status > 0) {
                    publicKey = result.body.publicKey;
                    exponent = result.body.exponent;
                    modulus = result.body.modulus;
                    console.log(exponent);
                    console.log(modulus);
                } else {
                    alert("sss");
                    alert(result.message);
                }
            },
            error: function () {
                alert("服务器故障，请刷新或稍后重试!");
            }
        });
    });
    function doRegister() {
        var crypt = new JSEncrypt();
        crypt.setPublicKey(publicKey);
        //pwd已经是base64加密的结果
        var pwd = (crypt.encrypt($('#password').val()));
        var url = "<%=request.getContextPath()%>/user/register.do";
        var user = {
            "username": $('#username').val(),
            "password": pwd,
            "email": $('#email').val()
        };
        $.ajax({
            type: "POST",
            url: url,
            data: user,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded",
            success: function (result) {
                if (result.status > 0) {
                    //跳转到激活界面
                    alert("注册成功，系统将发送激活邮件，请激活后登陆系统")
                    window.location.href = '<%=request.getContextPath()%>/login.jsp';
                } else {
                    alert(result.message);
                }
            }
        });
    }
</script>
</html>
