<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>标题</title>
</head>
<body>
    <a href='<c:url value="/user/findlist" />'>查看用户列表</a><br/>
    <a href='<c:url value="/user/toadd" />'>添加新用户</a>
</body>
</html>
