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
<fmt:message bundle="${loc}" key="button.logout" var="Logout"/>
<fmt:message bundle="${loc}" key="button.personalAccount" var="PersonalAccount"/>
<fmt:message bundle="${loc}" key="label.subscription" var="Subscription"/>
<fmt:message bundle="${loc}" key="label.feedback" var="Feedback"/>
<fmt:message bundle="${loc}" key="label.myProfile" var="MyProfile"/>
<fmt:message bundle="${loc}" key="label.myAccount" var="MyAccount"/>
<fmt:message bundle="${loc}" key="label.mySubscriptions" var="MySubscriptions"/>


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
                <a href="controller?command=show_publications&pageNumber=1" class="nav-link">${Publications}</a>
            </li>
            <li class="nav-item">
                <a href="controller?command=show_authors&pageNumber=1" class="nav-link">${Authors}</a>
            </li>
            <li class="nav-item">
                <a href="controller?command=show_genres&pageNumber=1" class="nav-link">${Genres}</a>
            </li>
            <li class="nav-item">
                <a href="controller?command=show_publication_types&pageNumber=1" class="nav-link">${PublicationTypes}</a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link">${Subscription}</a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link">${Feedback}</a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
            <a href="${pageContext.servletContext.contextPath}/controller?command=logout&pageNumber=1" type="button" class="btn btn-outline-success my-2 my-sm-0">${Logout}</a>
            <div class="dropdown open">
                <a class="nav-link dropdown-toggle" href="#" id="dropdownPersonalAccount" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">${PersonalAccount}</a>
                <div class="dropdown-menu" aria-labelledby="dropdownUsers">
                    <a class="dropdown-item" href="${pageContext.servletContext.contextPath}/controller?command=find_user">${MyProfile}</a>
                    <a class="dropdown-item" href="${pageContext.servletContext.contextPath}/controller?command=find_account_by_user">${MyAccount}</a>
                    <a class="dropdown-item" href="${pageContext.servletContext.contextPath}/controller?command=find_subscription_by_user">${MySubscriptions}</a>
                </div>
            </div>
        </form>
    </div>
</nav>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <ul class="navbar-nav ml-auto">
        <a href="controller?command=show_publications&language=ru_RU"> <img class="lang" src="../../images/rus_flag.jpg"></a>
        <a href="controller?command=show_publications&language=en_US"> <img class="lang" src="../../images/gbr_flag.jpg"></a>
    </ul>
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

