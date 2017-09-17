<%--
  Created by IntelliJ IDEA.
  User: author
  Date: 2017/9/17
  Time: 10:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>找回密码</title>
    <%@include file="common.jsp" %>
</head>
<body>
email：<input type="text" id="email"><span class="error"></span><br/>
<img id="code" class="code_pic" width="60" height="31" style="cursor:pointer;">
<a class="blurry" id="newPic" onclick="getPic();">看不清楚，换一张</a><br/>
<input type="text" name="code" id="codeInput" placeholder="验证码"><br/>
<button type="submit" onclick="doForgotPwd()">找回密码</button>
</body>
<script>
    var urlPic = "<%=request.getContextPath()%>/user/verfiyCode.do";
    $("#code").attr("src", urlPic);
    function getPic() {
        $("#code").attr("src", urlPic + "?flag=" + Math.random());
    }
    function doForgotPwd(){
        var user = {
            "email": $('#email').val(),
            "code":$("#codeInput").val()
        };
        var url = "<%=request.getContextPath()%>/user/forgotPwd.do";
        $.ajax({
            type: "POST",
            url: url,
            data: user,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded",
            success: function (result) {
                if (result.status > 0) {
                    //跳转到激活界面
                    alert("申请成功，系统将发送修改密码邮件，请根据邮件地址修改密码")
                } else {
                    alert(result.message);
                }
            }
        });
    }
</script>
</html>
