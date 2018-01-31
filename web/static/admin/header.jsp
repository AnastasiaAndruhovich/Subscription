<%--
Created by IntelliJ IDEA.
User: nastya
Date: 20.12.2017
Time: 0:02
To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="locale.locale" var="loc"/>
<fmt:message bundle="${loc}" key="label.publications" var="Publications"></fmt:message>
<fmt:message bundle="${loc}" key="label.genres" var="Genres"></fmt:message>
<fmt:message bundle="${loc}" key="label.publicationTypes" var="PublicationTypes"></fmt:message>
<fmt:message bundle="${loc}" key="label.authors" var="Authors"></fmt:message>
<fmt:message bundle="${loc}" key="label.logout" var="Logout"></fmt:message>
<fmt:message bundle="${loc}" key="label.personalAccount" var="PersonalAccount"></fmt:message>
<fmt:message bundle="${loc}" key="label.subscription" var="Subscription"></fmt:message>
<fmt:message bundle="${loc}" key="label.feedback" var="Feedback"></fmt:message>
<fmt:message bundle="${loc}" key="label.users" var="Users"></fmt:message>
<fmt:message bundle="${loc}" key="label.showUsers" var="ShowUsers"></fmt:message>
<fmt:message bundle="${loc}" key="label.blockUser" var="BlockUser"></fmt:message>
<fmt:message bundle="${loc}" key="label.unblockUser" var="UnblockUser"></fmt:message>
<fmt:message bundle="${loc}" key="label.showGenres" var="ShowGenres"></fmt:message>
<fmt:message bundle="${loc}" key="label.showPublications" var="ShowPublications"></fmt:message>
<fmt:message bundle="${loc}" key="label.showPublicationTypes" var="ShowPublicationTypes"></fmt:message>
<fmt:message bundle="${loc}" key="label.showAuthors" var="ShowAuthors"></fmt:message>
<fmt:message bundle="${loc}" key="label.addAuthor" var="AddAuthor"></fmt:message>
<fmt:message bundle="${loc}" key="label.addGenre" var="AddGenre"></fmt:message>
<fmt:message bundle="${loc}" key="label.addPublication" var="AddPublication"></fmt:message>
<fmt:message bundle="${loc}" key="label.addPublicationType" var="AddPublicationType"></fmt:message>

<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">

    <style>
        <%@include file="../../css/style.css" %>
    </style>
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
            <li>
                <div class="dropdown open">
                    <a class="nav-link dropdown-toggle" href="#" id="dropdownPublications" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">${Publications}</a>
                    <div class="dropdown-menu" aria-labelledby="dropdownPublications">
                        <a class="dropdown-item" href="controller?command=show_publications&pageNumber=1">
                            ${ShowPublications}</a>
                        <a class="dropdown-item" href="#">${AddPublication}</a>
                    </div>
                </div>
            </li>
            <li>
                <div class="dropdown open">
                    <a class="nav-link dropdown-toggle" href="#" id="dropdownAuthors" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">${Authors}</a>
                    <div class="dropdown-menu" aria-labelledby="dropdownAuthors">
                        <a class="dropdown-item" href="controller?command=show_authors&pageNumber=1">${ShowAuthors}</a>
                        <a class="dropdown-item" href="#">${AddAuthor}</a>
                    </div>
                </div>
            </li>
            <li>
                <div class="dropdown open">
                    <a class="nav-link dropdown-toggle" href="#" id="dropdownGenres" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">${Genres}</a>
                    <div class="dropdown-menu" aria-labelledby="dropdownGenres">
                        <a class="dropdown-item" href="controller?command=show_genres&pageNumber=1">${ShowGenres}</a>
                        <a class="dropdown-item" href="#">${AddGenre}</a>
                    </div>
                </div>
            </li>
            <li>
                <div class="dropdown open">
                    <a class="nav-link dropdown-toggle" href="#" id="dropdownPublicationTypes" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">${PublicationTypes}</a>
                    <div class="dropdown-menu" aria-labelledby="dropdownPublicationTypes">
                        <a class="dropdown-item" href="controller?command=show_publication_types&pageNumber=1">
                            ${ShowPublicationTypes}</a>
                        <a class="dropdown-item" href="#">${AddPublicationType}</a>
                    </div>
                </div>
            </li>
            <li>
                <div class="dropdown open">
                    <a class="nav-link dropdown-toggle" href="#" id="dropdownUsers" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">${Users}</a>
                    <div class="dropdown-menu" aria-labelledby="dropdownUsers">
                        <a class="dropdown-item" href="controller?command=show_users&pageNumber=1">${ShowUsers}</a>
                        <a class="dropdown-item" href="#">${BlockUser}</a>
                        <a class="dropdown-item" href="#">${UnblockUser}</a>
                    </div>
                </div>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link">${Subscription}</a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link">${Feedback}</a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
            <input type="text" class="form-control mr-sm-2" placeholder="Search" aria-label="Search">
            <button type="button" class="btn btn-outline-success my-2 my-sm-0">Searсh</button>
            <button type="button" class="btn btn-outline-success my-2 my-sm-0">${Logout}</button>
            <button type="button" class="btn btn-outline-success my-2 my-sm-0">${PersonalAccount}</button>
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
</html>
﻿

