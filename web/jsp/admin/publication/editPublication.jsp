<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 26.01.2018
  Time: 18:07
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
<fmt:message bundle="${loc}" key="label.editPublication" var="Title"/>
<fmt:message bundle="${loc}" key="label.publicationType" var="Type"/>
<fmt:message bundle="${loc}" key="label.genre" var="Genre"/>
<fmt:message bundle="${loc}" key="label.author" var="Authors"/>
<fmt:message bundle="${loc}" key="label.publisher" var="Publisher"/>
<fmt:message bundle="${loc}" key="label.price" var="Price"/>
<fmt:message bundle="${loc}" key="label.moneyUnit" var="MoneyUnit"/>
<fmt:message bundle="${loc}" key="label.description" var="Description"/>
<fmt:message bundle="${loc}" key="label.name" var="Name"/>
<fmt:message bundle="${loc}" key="label.authorLastName" var="AuthorLastName"/>
<fmt:message bundle="${loc}" key="label.authorFirstName" var="AuthorFirstName"/>
<fmt:message bundle="${loc}" key="button.edit" var="Edit"/>
<fmt:message bundle="${loc}" key="message.informationIsAbsent" var="InformationIsAbsent"/>

<html lang="en">
<head>
    <title>Edit Publication</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">

    <jsp:useBean id="publication" scope="session" type="by.andruhovich.subscription.entity.Publication"/>
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
                <div class="publication card">
                    <c:choose>
                        <c:when test="${publication!=null}">
                            <div class="publication card">
                                <form name="editForm" method="POST"
                                      action="${pageContext.servletContext.contextPath}/controller">
                                    <input type="hidden" name="command" value="edit_publication"/>
                                    <input type="hidden" name="publicationId" value="${publication.publicationId}"/>
                                    <p>${sessionScope.message}</p>
                                    <div class="form-group row">
                                        <label for="name" class="col-sm-3 col-form-label">${Name}</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" name="name" id="name" required=""
                                                   value="${publication.name}" pattern="([а-яёА-ЯЁ]|[a-zA-Z]|\s|-){1,30}"
                                                   title="Publication name must be between 1 and 30 characters, contain only
                                                   alphabetic characters."/>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="publicationType" class="col-sm-3 col-form-label">${Type}</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" name="publicationType"
                                                   id="publicationType" required=""
                                                   value="${publication.publicationType.name}"
                                                   pattern="([а-яёА-ЯЁ]|[a-zA-Z]|\s|-){1,30}"
                                                   title="Publication type must be between 1 and 30 characters, contain only
                                                   alphabetic characters."/>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="genre" class="col-sm-3 col-form-label">${Genre}</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" name="genre" id="genre" required=""
                                                   value="${publication.genre.name}" pattern="([а-яёА-ЯЁ]|[a-zA-Z]|\s|-){1,30}"
                                                   title="Genre must be between 1 and 30 characters, contain only
                                                   alphabetic characters."/>
                                        </div>
                                    </div>
                                    <c:forEach var="author" items="${publication.authors}">
                                        <div class="form-group row">
                                            <label for="lastName"
                                                   class="col-sm-3 col-form-label">${AuthorLastName}</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" name="lastName"
                                                       id="lastName" value="${author.authorLastName}"
                                                       pattern="([а-яёА-ЯЁ]|[a-zA-Z]|\s|-){1,30}"
                                                       title="Last name must be between 1 and 30 characters,
                                                       contain only alphabetic characters."/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label for="firstName"
                                                   class="col-sm-3 col-form-label">${AuthorFirstName}</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" name="firstName"
                                                       id="firstName" value="${author.authorFirstName}"
                                                       pattern="([а-яёА-ЯЁ]|[a-zA-Z]|\s|-){1,30}"
                                                       title="First name must be between 1 and 30 characters,
                                                       contain only alphabetic characters."/>
                                            </div>
                                        </div>
                                    </c:forEach>
                                    <div class="form-group row">
                                        <label for="publisherName"
                                               class="col-sm-3 col-form-label">${Publisher}</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" name="publisherName"
                                                   id="publisherName"
                                                   value="${publication.authors[0].publisherName}"
                                                   pattern="([а-яёА-ЯЁ]|[a-zA-Z]|\s|-){1,30}"
                                                   title="Publisher name must be between 1 and 30 characters,
                                                   contain only alphabetic characters."/>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="description" class="col-sm-3 col-form-label">${Description}</label>
                                        <div class="col-sm-9">
                                            <textarea class="form-control" name="description" id="description"
                                                      required="" value="${publication.description}"></textarea>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="price" class="col-sm-3 col-form-label">${Price} ${MoneyUnit}</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" name="price" id="price" required=""
                                                   value="${publication.price}"
                                                   pattern="^[\d]+?\.[\d]{2}$" placeholder="0.00"/>
                                        </div>
                                    </div>
                                    <button class="btn btn-outline-success my-2 my-sm-0">${Edit}</button>
                                </form>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p>${InformationIsAbsent}</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="col-1"></div>
        </div>
    </div>
</div>

<%@include file="../../../static/common/footer.jsp" %>

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
