<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style><%@include file='/bootstrap-3.3.7/css/bootstrap.css'%></style>
    <title>ToDo-Registration</title>
</head>
<body>
    <c:if test="${empty isSuccess}">
        <jsp:forward page="/index.jsp"/>
    </c:if>
    <article>
        <%@ include  file="successfulRegistration.html"%>
    </article>

    <footer>
        <%@ include  file="footer.html"%>
    </footer>
</body>
</html>
