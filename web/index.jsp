<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style><%@include file='/bootstrap-3.3.7/css/bootstrap.css'%></style>
    <title>ToDo</title>
</head>
<body>
<header>
    <div align="center">
        <jsp:include page="header.jsp"/>
    </div>
</header>
<article>
    <div align="center">
        <c:if test="${not empty user}">
            <form method="get">
                <input type="submit" class="btn btn-lg btn-primary" value="Go to all tasks"
                       formaction="<c:url value='/operation/updatetasks'/>">
            </form>
        </c:if>
    </div>
</article>
<footer>
    <%@ include  file="footer.html"%>
</footer>
</body>
</html>