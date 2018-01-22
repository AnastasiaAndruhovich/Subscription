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

<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
</head>
<body>
<%@include file="../../part/user/header.html" %>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-2"></div>
            <div class="col-8">
                <div class="publication card">
                    <c:choose>
                        <c:when test="${publications!=null}">
                            <c:forEach var="publication" items="${publications}">
                                <div class="container">
                                    <div class="row">
                                        <div class="col-6">
                                            <img src="http://bookashka.name/fb2imgs/e1/e13744dcc4b253fbc4f826e9fb27157d.jpg" alt=""
                                                 class="w-100">
                                        </div>
                                        <div class="col-6">
                                            <p class="title">Name: ${publication.name}</p>
                                            <p class="title">Type: ${publication.publicationType.name}</p>
                                            <p class="title">Genre: ${publication.genre.name}</p>
                                            <c:when test="${publication.authors!=null}">
                                                <p class="title">Authors:
                                                    <c:forEach var="author" items="${publication.authors}">
                                                        ${author.authorLastName} ${author.authorFirstName}
                                                    </c:forEach>
                                                </p>
                                                <p class="title">Publisher: ${publication.authors[0].publisherName}</p>
                                            </c:when>
                                            <p class="title">Description: ${publication.description}</p>
                                            <p class="title">Price: ${publication.price} BY</p>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p>${infromationIsAbsent}</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="col-2"></div>
        </div>
    </div>
</div>

<%@include file="../../part/common/numeration.html" %>
<%@include file="../../part/common/footer.html" %>

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

