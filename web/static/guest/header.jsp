<%--
Created by IntelliJ IDEA.
User: nastya
Date: 20.12.2017
Time: 0:02
To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="locale.locale" var="loc"/>
<fmt:message bundle="${loc}" key="label.publications" var="Publications"/>
<fmt:message bundle="${loc}" key="label.genres" var="Genres"/>
<fmt:message bundle="${loc}" key="label.publicationTypes" var="PublicationTypes"/>
<fmt:message bundle="${loc}" key="label.authors" var="Authors"/>
<fmt:message bundle="${loc}" key="button.login" var="Login"/>
<fmt:message bundle="${loc}" key="button.signUp" var="SignUp"/>
<fmt:message bundle="${loc}" key="label.subscription" var="Subscription"/>
<fmt:message bundle="${loc}" key="label.feedback" var="Feedback"/>

<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">

    <style><%@include file="../../css/style.css"%></style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
    <a href="" class="navbar-brand">
        <img src="http://photos3.fotosearch.com/bthumb/CSP/CSP628/k20713555.jpg" alt="logo" width="30" height="30">
    </a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a href="${pageContext.servletContext.contextPath}/controller?command=show_publications&pageNumber=1" class="nav-link">${Publications}</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.servletContext.contextPath}/controller?command=show_authors&pageNumber=1" class="nav-link">${Authors}</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.servletContext.contextPath}/controller?command=show_genres&pageNumber=1" class="nav-link">${Genres}</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.servletContext.contextPath}/controller?command=show_publication_types&pageNumber=1" class="nav-link">${PublicationTypes}</a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
            <a href="${pageContext.servletContext.contextPath}/jsp/user/user/login.jsp" type="button" class="btn btn-outline-success my-2 my-sm-0">${Login}</a>
            <a href="${pageContext.servletContext.contextPath}/jsp/user/user/signUp.jsp" type="button" class="btn btn-outline-success my-2 my-sm-0">${SignUp}</a>
        </form>
    </div>
</nav>

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
</html>﻿

