<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 25.01.2018
  Time: 14:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtag"%>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="locale.locale" var="loc"/>
<fmt:message bundle="${loc}" key="label.signUp" var="Title"/>
<fmt:message bundle="${loc}" key="label.registerForm" var="RegisterForm"/>
<fmt:message bundle="${loc}" key="label.userLastName" var="LastName"/>
<fmt:message bundle="${loc}" key="label.userFirstName" var="FirstName"/>
<fmt:message bundle="${loc}" key="label.login" var="Login"/>
<fmt:message bundle="${loc}" key="label.password" var="Password"/>
<fmt:message bundle="${loc}" key="label.repeatPassword" var="RepeatPassword"/>
<fmt:message bundle="${loc}" key="label.birthDate" var="BirthDate"/>
<fmt:message bundle="${loc}" key="label.address" var="Address"/>
<fmt:message bundle="${loc}" key="label.city" var="City"/>
<fmt:message bundle="${loc}" key="label.postalIndex" var="PostalIndex"/>
<fmt:message bundle="${loc}" key="button.edit" var="Edit"/>
<fmt:message bundle="${loc}" key="button.changePassword" var="ChangePassword"/>

<html lang="en">
<head>
    <title>Profile</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">

    <jsp:useBean id="user" scope="session" type="by.andruhovich.subscription.entity.User"/>
</head>
<body>
<ctg:role/>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <c:choose>
                    <c:when test="${user!=null}">
                        ${sessionScope.message}
                        <form name="loginForm" method="POST" action="${pageContext.servletContext.contextPath}/controller">
                            <input type="hidden" name="command" value="update_user"/>

                            <!-- Text input-->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="lastName">${LastName}</label>
                                <div class="col-sm-9">
                                    <input id="lastName" name="lastName" type="text" value="${user.lastName}"
                                           class="form-control input-md" required=""
                                           pattern="([а-яёА-ЯЁ]|[a-zA-Z]){1,30}"
                                           title="Last name must be between 1 and 30 characters, contain only
                                           alphabetic characters.">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="firstName">${FirstName}</label>
                                <div class="col-sm-9">
                                    <input id="firstName" name="firstName" type="text" value="${user.firstName}"
                                           class="form-control input-md" required="" pattern="([а-яёА-ЯЁ]|[a-zA-Z]){1,30}"
                                           title="First name must be between 1 and 30 characters, contain only
                                           alphabetic characters.">
                                </div>
                            </div>

                            <!--Login -->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="registerLogin">${Login}</label>
                                <div class="col-sm-9">
                                    <input id="registerLogin" name="login" type="text" value="${user.login}"
                                           class="form-control input-md" required=""
                                           pattern="^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){6,30}[a-zA-Z0-9]$"
                                           title="Login must be between 6 and 30 characters, contain only digit,
                                               alphabetic and '.' or '_' characters. Characters '.' and '_' can't be
                                               matching at the begin or at the end. Also combinations ._ or __ or _.
                                               are not valid.">
                                </div>
                            </div>

                            <!-- Select Date Of Birth-->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="birthDate">${BirthDate}</label>
                                <div class="col-sm-9">
                                    <input id="birthDate" name="birthDate" type="text" value="${user.birthDate}"
                                           class="form-control input-md" required=""
                                           pattern="((19|20)\d\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])">
                                </div>
                            </div>

                            <!-- Address input-->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="address">${Address}</label>
                                <div class="col-sm-9">
                                    <input id="address" name="address" type="text" value="${user.address}"
                                           class="form-control input-md" required="">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="city">${City}</label>
                                <div class="col-sm-9">
                                    <input id="city" name="city" type="text" value="${user.city}"
                                           class="form-control input-md" required="" pattern="([а-яёА-ЯЁ]|[a-zA-Z]){1,30}"
                                           title="City name must be between 1 and 30 characters, contain only
                                           alphabetic characters.">
                                </div>
                            </div>

                            <!-- Postal index input-->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="postalIndex">${PostalIndex}</label>
                                <div class="col-sm-9">
                                    <input id="postalIndex" name="postalIndex" type="text" value="${user.postalIndex}"
                                           class="form-control input-md" required="" pattern="[0-9]{6}">
                                </div>
                            </div>

                            <button class="btn btn-primary">${Edit}</button>
                        </form>
                        <a href="${pageContext.servletContext.contextPath}/jsp/user/user/changePassword.jsp" type="button" class="btn btn-outline-success my-2 my-sm-0">${ChangePassword}</a>
                    </c:when>
                </c:choose>
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
