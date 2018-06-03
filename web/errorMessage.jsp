<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style><%@include file='/bootstrap-3.3.7/css/bootstrap.css'%></style>
    <title>ErrorMessage</title>
</head>
<body>
    <div class="row">
        <div class="form-group col-xs-4"></div>
        <div class="form-group col-xs-4 center-block">
            <div class="control-group required">
                <div id="errorMessage" class="alert alert-danger" role="alert">
                    ${message}
                </div>
                <script>
                    var errorMessage = document.getElementById('errorMessage');
                    var value = errorMessage.innerHTML;
                    var reg = /\w/;
                    if(value.search(reg) == -1){
                        errorMessage.style.display = "none";
                    }
                </script>
            </div>
        </div>
    </div>
</body>
</html>
