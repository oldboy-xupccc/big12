<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>修改用户</title>
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
        input{
            width: 100%;
        }
    </style>
</head>
<body>
    <form action='<c:url value="/user/update" />' method="post">
        <table>
            <input type="hidden" name="id" value='<c:out  value="${user.id}"/>'>
            <tr>
                <td>用户名</td>
                <td><input type="text" name="name" value='<c:out value="${user.name}" />'></td>
            </tr>
            <tr>
                <td>年龄</td>
                <td><input type="text" name="age" value='<c:out value="${user.age}" />'></td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: right">
                    <input type="submit" value="提交">
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
