<%--
  Created by IntelliJ IDEA.
  User: author
  Date: 2017/9/16
  Time: 16:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改邮箱</title>
</head>
<body>
email：<input type="text" id="email"><span class="error"></span><br/>
<button type="submit" onclick="updateEmail()">注册</button>
</body>
<script>
    function doRequestActive() {
        var url = "<%=request.getContextPath()%>/user/updateEmail.do";
        $.ajax({
            type: "POST",
            url: url,
            data: {"email": $('#email').val()},
            dataType: "json",
            contentType: "application/x-www-form-urlencoded",
            success: function (result) {
                if (result.status > 0) {
                    alert("修改邮箱成功，请重新激活账号")
                } else {
                    alert(result.message);
                }
            }
        })
    }
</script>
</html>
