<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 31.01.2018
  Time: 16:26
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
<fmt:message bundle="${loc}" key="label.addPublication" var="Title"></fmt:message>
<fmt:message bundle="${loc}" key="label.publicationType" var="Type"></fmt:message>
<fmt:message bundle="${loc}" key="label.genre" var="Genre"></fmt:message>
<fmt:message bundle="${loc}" key="label.author" var="Authors"></fmt:message>
<fmt:message bundle="${loc}" key="label.publisher" var="Publisher"></fmt:message>
<fmt:message bundle="${loc}" key="label.price" var="Price"></fmt:message>
<fmt:message bundle="${loc}" key="label.moneyUnit" var="MoneyUnit"></fmt:message>
<fmt:message bundle="${loc}" key="label.description" var="Description"></fmt:message>
<fmt:message bundle="${loc}" key="label.name" var="Name"></fmt:message>
<fmt:message bundle="${loc}" key="label.authorLastName" var="AuthorLastName"></fmt:message>
<fmt:message bundle="${loc}" key="label.authorFirstName" var="AuthorFirstName"></fmt:message>
<fmt:message bundle="${loc}" key="button.uploadPicture" var="UploadPicture"></fmt:message>

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
    <style><%@include file="../../css/uploadImage.css"%></style>

</head>
<body>
<ctg:role></ctg:role>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div class="publication card">
                    <div class="row">
                        <div class="col-4">
                            <form name="addForm" method="POST" action="controller?command=add_publication_picture" enctype="multipart/form-data">
                                <input type="file" id="files" name="files[]" accept=".jpg,.png"/>
                                <script type="text/javascript" src="../../js/uploadImage.js"></script>
                                <button class="btn btn-outline-success my-2 my-sm-0">${UploadPicture}</button>
                            </form>
                        </div>
                        <div class="col-8">
                            <div class="publication card">
                                <c:choose>
                                    <c:when test="${publications!=null}">
                                        <c:forEach var="publication" items="${publications}">
                                            <div class="container">
                                                <div class="row">
                                                    <div class="col-4">
                                                        <img src="http://bookashka.name/fb2imgs/e1/e13744dcc4b253fbc4f826e9fb27157d.jpg"
                                                             alt="" class="w-100">
                                                    </div>
                                                    <div class="col-8">
                                                        <p>${Name}: ${publication.name}</p>
                                                        <p>${Type}: ${publication.publicationType.name}</></p>
                                                        <p>${Genre}: ${publication.genre.name}</p>
                                                        <c:choose>
                                                            <c:when test="${publication.authors!=null}">
                                                                <div class="row">${Authors}:
                                                                    <c:forEach var="author" items="${publication.authors}">
                                                                        ${author.authorLastName} ${author.authorFirstName}</a>
                                                                    </c:forEach>
                                                                </div>
                                                                <p>${Publisher}: ${publication.authors[0].publisherName}</p>
                                                            </c:when>
                                                        </c:choose>
                                                        <p>${Description}: ${publication.description}</p>
                                                        <p>${Price}: ${publication.price} ${MoneyUnit}</p>
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
                    </div>
                </div>
            </div>
            <div class="col-1"></div>
        </div>
    </div>
</div>

<%@include file="../../static/common/footer.html" %>

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
