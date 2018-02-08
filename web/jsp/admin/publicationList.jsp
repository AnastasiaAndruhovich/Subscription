<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 26.01.2018
  Time: 14:42
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
<fmt:message bundle="${loc}" key="label.publications" var="Title"/>
<fmt:message bundle="${loc}" key="label.publicationType" var="Type"/>
<fmt:message bundle="${loc}" key="label.genre" var="Genre"/>
<fmt:message bundle="${loc}" key="label.author" var="Authors"/>
<fmt:message bundle="${loc}" key="label.publisher" var="Publisher"/>
<fmt:message bundle="${loc}" key="label.price" var="Price"/>
<fmt:message bundle="${loc}" key="label.moneyUnit" var="MoneyUnit"/>
<fmt:message bundle="${loc}" key="label.description" var="Description"/>
<fmt:message bundle="${loc}" key="label.name" var="Name"/>
<fmt:message bundle="${loc}" key="button.subscribe" var="Subscribe"/>
<fmt:message bundle="${loc}" key="button.edit" var="Edit"/>
<fmt:message bundle="${loc}" key="button.delete" var="Delete"/>
<fmt:message bundle="${loc}" key="message.informationIsAbsent" var="InformationIsAbsent"/>

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
                <div class="publication card">
                    ${requestScope.message}
                    <c:choose>
                        <c:when test="${requestScope.publications!=null}">
                            <c:forEach var="publication" items="${requestScope.publications}">
                                <div class="container">
                                    <div class="row">
                                        <div class="col-4">
                                            <ctg:image publicationId="${publication.publicationId}"/>
                                        </div>
                                        <div class="col-8">
                                            <p>${Name}: ${publication.name}</p>
                                            <p>
                                                ${Type}:<a href="${pageContext.servletContext.contextPath}/controller?command=find_publication_by_publication_type&publicationTypeId=${publication.publicationType.publicationTypeId}"> ${publication.publicationType.name}</a>
                                            </p>
                                            <p>
                                                ${Genre}:<a href="${pageContext.servletContext.contextPath}/controller?command=find_publication_by_genre&genreId=${publication.genre.genreId}"> ${publication.genre.name}</a>
                                            </p>
                                            <c:choose>
                                                <c:when test="${publication.authors!=null}">
                                                    <div class="row">
                                                        ${Authors}:
                                                        <c:forEach var="author" items="${publication.authors}">
                                                            <a href="${pageContext.servletContext.contextPath}/controller?command=find_publication_by_author&authorId=${author.authorId}"> ${author.authorLastName} ${author.authorFirstName}</a>
                                                        </c:forEach>
                                                    </div>
                                                    <p>${Publisher}: ${publication.authors[0].publisherName}</p>
                                                </c:when>
                                            </c:choose>
                                            <p>${Description}: ${publication.description}</p>
                                            <p>${Price}: ${publication.price} ${MoneyUnit}</p>
                                            <div class="row">
                                                <form method="GET" action="${pageContext.servletContext.contextPath}/controller">
                                                    <input type="hidden" name="command" value="add_subscription"/>
                                                    <input type="hidden" name="publicationId" value="${publication.publicationId}">
                                                    <button class="btn btn-outline-success my-2 my-sm-0">${Subscribe}</button>
                                                </form>
                                                <form method="POST" action="${pageContext.servletContext.contextPath}/controller">
                                                    <input type="hidden" name="command" value="parse_publication"/>
                                                    <input type="hidden" name="publicationId" value="${publication.publicationId}"/>
                                                    <button class="btn btn-outline-warning my-2 my-sm-0">${Edit}</button>
                                                </form>
                                                <button class="btn btn-outline-danger my-2 my-sm-0" data-target="#publicationModal" data-toggle="modal">${Delete}</button>
                                                <div class="modal fade" id="publicationModal" tabindex="-1" role="dialog" aria-labelledby="publicationModal" aria-hidden="true">
                                                    <div class="modal-dialog modal-sm" role="document">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                                    <span aria-hidden="true">&times;</span>
                                                                </button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <div class="container-fluid">
                                                                    <p>WARNING!</p>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <form method="POST" action="${pageContext.servletContext.contextPath}/controller">
                                                                    <input type="hidden" name="command" value="delete_publication"/>
                                                                    <input type="hidden" name="publicationId" value="${publication.publicationId}">
                                                                    <button class="btn btn-outline-danger my-2 my-sm-0">${Delete}</button>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
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
