<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 29.01.2018
  Time: 23:16
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
<fmt:message bundle="${loc}" key="button.signUp" var="SignUp"/>

<html lang="en">
<head>
    <title>${Title}</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">

    <style><%@include file="../../css/style.css"%></style>
</head>
<body>
<ctg:role/>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div class="sign-up card">
                    <h1 class="title" id="registerTitle">${RegisterForm}</h1>
                    <div class="error-sign-up">${requestScope.errorSignUp}</div>
                    <div class="row">
                        <div class="col-12">
                            <form name="loginForm" method="POST" action="${pageContext.servletContext.contextPath}/controller">
                                <input type="hidden" name="command" value="sign_up"/>

                                <!-- Text input-->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="lastName">${LastName}</label>
                                    <div class="col-sm-9">
                                        <input id="lastName" name="lastName" type="text" placeholder="LastName"
                                               class="form-control input-md" required="">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="firstName">${FirstName}</label>
                                    <div class="col-sm-9">
                                        <input id="firstName" name="firstName" type="text" placeholder="FirstName"
                                               class="form-control input-md" required="">
                                    </div>
                                </div>

                                <!--Login -->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="registerLogin">${Login}</label>
                                    <div class="col-sm-9">
                                        <input id="registerLogin" name="login" type="text" placeholder="Login"
                                               class="form-control input-md" required=""
                                               pattern="^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){6,18}[a-zA-Z0-9]$"
                                               title="Login must be between 6 and 18 characters, contain only digit,
                                               alphabetic and '.' or '_' characters. Characters '.' and '_' can't be
                                               matching at the begin or at the end. Also combinations ._ or __ or _.
                                               are not valid.">
                                    </div>
                                </div>

                                <!--Password -->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="password">${Password}</label>
                                    <div class="col-sm-9">
                                        <input id="password" name="password" type="password" placeholder="Password"
                                               class="form-control input-md" required=""
                                               pattern="(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{3,15})$"
                                               title="Password must be between 8 and 10 characters, contain at least
                                               one digit and one alphabetic character, and must not contain special
                                               characters.">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="confirmPassword">${RepeatPassword}</label>
                                    <div class="col-sm-9">
                                        <input id="confirmPassword" name="confirmPassword" type="password"
                                               placeholder="Repeat password" class="form-control input-md" required=""
                                               pattern="(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{3,15})$">
                                    </div>
                                </div>

                                <!-- Select Date Of Birth-->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="birthDate">${BirthDate}</label>
                                    <div class="col-sm-9">
                                        <input id="birthDate" name="birthDate" type="text" placeholder="yyyy-mm-dd"
                                               class="form-control input-md" required=""
                                               pattern="((19|20)\d\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])">
                                    </div>
                                </div>

                                <!-- Address input-->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="address">${Address}</label>
                                    <div class="col-sm-9">
                                        <input id="address" name="address" type="text" placeholder="Address"
                                               class="form-control input-md" required="">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="city">${City}</label>
                                    <div class="col-sm-9">
                                        <input id="city" name="city" type="text" placeholder="City"
                                               class="form-control input-md" required="">
                                    </div>
                                </div>

                                <!-- Postal index input-->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="postalIndex">${PostalIndex}</label>
                                    <div class="col-sm-9">
                                        <input id="postalIndex" name="postalIndex" type="text" placeholder="XXXXXX"
                                               class="form-control input-md" required="" pattern="[0-9]{6}">
                                    </div>
                                </div>

                                <button class="btn btn-primary">${SignUp}</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-1"></div>
        </div>
    </div>
</div>

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
