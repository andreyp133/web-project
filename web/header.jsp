<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <div>
        <div class="row">
            <div class="col-xs-12" align="center">
                    <h4>
                        <c:if test="${not empty user}">
                            <form method="post">
                                Current user:<span class="label label-default">${user.login}</span>
                                <input type="submit" class="btn btn-success" value="Log out"
                                       formaction="<c:url value='/logout'/>">
                                <hr>
                            </form>
                        </c:if>
                        <c:if test="${empty user}">
                            <form method="post">
                                Current user:<span class="label label-default">Guest</span>
                                <p><%@ include  file="/mainbutton.html"%><hr></p>
                            </form>
                        </c:if>
                    </h4>
            </div>
        </div>
    </div>
</body>
</html>
