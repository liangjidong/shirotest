<%--
  Created by IntelliJ IDEA.
  User: author
  Date: 2017/9/17
  Time: 14:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>邮箱找回密码</title>
    <%@include file="common.jsp" %>
</head>
<body>
用户名:<input id="username" name="username" readonly="readonly" value="${username}"/><br/>
密 码:<input id="password" name="password"/><br/>
<button type="submit" onclick="doCommit()">确认</button>
</body>
<script>
    function doCommit() {
        var user = {
            "username": $('#username').val(),
            "password": $("#password").val(),
            "checkCode":"${checkCode}"
        };
        var url = "<%=request.getContextPath()%>/user/changePwdByEmial.do";
        $.ajax({
            type: "POST",
            url: url,
            data: user,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded",
            success: function (result) {
                if (result.status > 0) {
                    alert("修改成功");
                    window.location.href = '<%=request.getContextPath()%>/login.jsp';
                } else {
                    alert(result.message);
                }
            }
        });
    }
</script>
</html>
