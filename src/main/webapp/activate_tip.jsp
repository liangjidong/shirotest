<%--
  Created by IntelliJ IDEA.
  User: author
  Date: 2017/9/16
  Time: 15:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>激活提醒</title>
    <%@include file="common.jsp" %>
</head>
<body>
<shiro:guest>
    欢迎游客访问，<a href="${pageContext.request.contextPath}/login.jsp">点击登录</a><br/>
</shiro:guest>
<shiro:user>
    欢迎[<shiro:principal/>]登录，<a href="${pageContext.request.contextPath}/logout.do">点击退出</a><br/>
    尚未激活，请到邮箱中激活邮件。若邮件过期，请点击<button type="submit" onclick="doRequestActive()">申请激活链接</button>系统将重新发送激活链接到你的邮箱。<br/>
    如果注册时填写的邮箱账户不正确，请点击<a href="<%=request.getContextPath()%>/updateEmail.jsp">修改email</a>，填写正确的email，系统将重新发送激活链接到你的邮箱
</shiro:user>
</body>
<script>
    function doRequestActive() {
        var url = "<%=request.getContextPath()%>/user/request_active.do";
        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function (result) {
                alert(result.message);
            }
        })
    }
</script>
</html>
