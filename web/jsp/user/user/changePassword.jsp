<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 07.02.2018
  Time: 18:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtag" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="locale.locale" var="loc"/>
<fmt:message bundle="${loc}" key="label.changePassword" var="Title"/>
<fmt:message bundle="${loc}" key="label.oldPassword" var="OldPassword"/>
<fmt:message bundle="${loc}" key="label.newPassword" var="NewPassword"/>
<fmt:message bundle="${loc}" key="label.repeatPassword" var="RepeatPassword"/>
<fmt:message bundle="${loc}" key="button.change" var="Change"/>

<html lang="en">
<head>
    <title>${Title}</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">

    <style>
        <%@include file="../../../css/style.css" %>
    </style>
</head>
<body>
<ctg:role/>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div class="author card">
                    ${sessionScope.message}
                    <form name="changePasswordForm" method="POST"
                          action="${pageContext.servletContext.contextPath}/controller">
                        <input type="hidden" name="command" value="change_password"/>
                        <p>${sessionScope.result}</p>
                        <div class="form-group row">
                            <label for="oldPassword" class="col-sm-2 col-form-label">${OldPassword}</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control" name="oldPassword" id="oldPassword"
                                       placeholder="Old password" required=""/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="newPassword" class="col-sm-2 col-form-label">${NewPassword}</label>
                            <div class="col-sm-10">
                                <input id="newPassword" name="newPassword" type="password" placeholder="New password"
                                       class="form-control input-md" required=""
                                       pattern="(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{6,18})$"
                                       title="Password must be between 6 and 18 characters, contain at least
                                               one digit and one alphabetic character, and must not contain special
                                               characters.">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="repeatPassword" class="col-sm-2 col-form-label">${RepeatPassword}</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control" name="repeatPassword" id="repeatPassword"
                                       placeholder="Repeat password" required=""/>
                            </div>
                        </div>
                        <button class="btn btn-outline-success my-2 my-sm-0">${Change}</button>
                    </form>
                </div>
            </div>
            <div class="col-1"></div>
        </div>
    </div>
</div>

<%@include file="../../../static/common/footer.html" %>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous">
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
        integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous">
</script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
        integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous">
</script>
</body>
</html>
