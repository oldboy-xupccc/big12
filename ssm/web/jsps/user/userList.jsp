<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>userList</title>
    <style type="text/css">
        body{
            margin: 5px auto;
        }

        table{
            border-collapse: collapse;
            width: 50%;
        }

        td{
            border : 1px solid blue ;
            text-align: center;
        }
    </style>
</head>
<body>
    <a href='<c:url value="/user/toadd" />'>添加新用户</a><br/>
    <table border="0px solid blue"
           cellpadding="0px"
           cellspacing="0px">
        <tr>
            <td>ID</td>
            <td>Name</td>
            <td>Age</td>
            <td>删除</td>
            <td>编辑</td>
        </tr>
        <c:forEach items="${users}" var="u">
            <tr>
                <td><c:out value="${u.id}"/></td>
                <td><c:out value="${u.name}"/></td>
                <td><c:out value="${u.age}"/></td>
                <td><a href='<c:url value="/user/delete?uid=${u.id}" />'>删除</a></td>
                <td><a href='<c:url value="/user/edit?uid=${u.id}" />'>编辑</a></td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="5" style="text-align: right">
                <c:forEach end="${pages}" begin="1" var="i">
                    <c:if test="${i == param['pid']}">
                        [<c:out value="${i}" />]&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${i != param['pid']}">
                        <a href='<c:url value="/user/findlist?pid=${i}" />'><c:out  value="${i}"/></a>&nbsp;&nbsp;
                    </c:if>
                </c:forEach>
            </td>
        </tr>
    </table>
</body>
</html>
