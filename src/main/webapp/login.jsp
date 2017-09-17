<%--
  Created by IntelliJ IDEA.
  User: author
  Date: 2017/9/15
  Time: 23:08
  To change this template use File | Settings | File Templates.
  1，获取RSA公钥
  2，对密码进行RSA加密
  3，发送登陆请求
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%@include file="common.jsp" %>
</head>
<body>
用户名:<input id="username" name="username">
密 码:<input id="password" name="password">
<button type="submit" onclick="doLogin()">登录</button><br/>
<a href="<%=request.getContextPath()%>/register.jsp">注册</a>
<a href="<%=request.getContextPath()%>/forgotPwd.jsp">忘记密码</a>
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
                    alert(result.message);
                }
            },
            error: function () {
                alert("服务器故障，请刷新或稍后重试!");
            }
        });
    });
    function doLogin() {
//        setMaxDigits(131);
//        var key = new RSAKeyPair(exponent,"",modulus);
//        var encry = encryptedString(key, $('#password').val());
//        console.log(encry)
//        var pwd = btoa(encry);
//        console.log(pwd)
        var crypt = new JSEncrypt();
        crypt.setPublicKey(publicKey);
        //pwd已经是base64加密的结果
        var pwd = (crypt.encrypt($('#password').val()));
        alert(pwd);

        var user = {
            "username": $('#username').val(),
            "password": pwd
        };
        var url = "<%=request.getContextPath()%>/user/login.do";
        $.ajax({
            type: "POST",
            url: url,
            data: user,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded",
            success: function (result) {
                if (result.status > 0) {
                    window.location.href = '<%=request.getContextPath()%>/index.jsp';
                } else {
                    alert(result.message);
                    if(result.status==-7){
                        //未激活，跳转到提示激活页面
                        window.location.href = '<%=request.getContextPath()%>/activate_tip.jsp';
                    }
                }
            }
        });
    }
</script>
</html>
