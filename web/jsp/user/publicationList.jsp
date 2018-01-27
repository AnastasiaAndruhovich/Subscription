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
    <title>Publications</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">

    <style><%@include file="../../css/style.css"%></style>
</head>
<body>
<%@include file="../../static/user/header.html" %>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
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
                                            <p>Name: ${publication.name}</p>
                                            <p>
                                                Type:<a href="controller?command=find_publications_by_publication_type&publicationTypeId=${publication.publicationType.publicationTypeId}"> ${publication.publicationType.name}</a>
                                            </p>
                                            <p>
                                                Genre:<a href="controller?command=find_publications_by_genre&genreId=${publication.genre.genreId}"> ${publication.genre.name}</a>
                                            </p>
                                            <c:choose>
                                                <c:when test="${publication.authors!=null}">
                                                    <div class="row">
                                                        Authors:
                                                        <c:forEach var="author" items="${publication.authors}">
                                                            <a href="controller?command=find_publications_by_author&authorId=${author.authorId}"> ${author.authorLastName} ${author.authorFirstName}</a>
                                                        </c:forEach>
                                                    </div>
                                                    <p>Publisher: ${publication.authors[0].publisherName}</p>
                                                </c:when>
                                            </c:choose>
                                            <p>Description: ${publication.description}</p>
                                            <p>Price: ${publication.price} BY/month</p>
                                            <form method="GET" action="controller">
                                                <input type="hidden" name="command" value="redirect_add_subscription"/>
                                                <button class="btn btn-outline-success my-2 my-sm-0">Subscribe</button>
                                            </form>
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
            <div class="col-1"></div>
        </div>
    </div>
</div>

<%--Declare variable for current page and count of contacts --%>
<c:set var="currentPage" value="${(pageNumber==null) ? 1 : pageNumber}"/>
<c:set var="lastPage" value="${(pageCount==null) ? 1 : pageCount}"/>

<nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
        <c:choose>
            <c:when test="${currentPage==1}">
                <li class="page-item disabled">
                    <a class="page-link" href="#" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                        <span class="sr-only">Previous</span>
                    </a>
                </li>
            </c:when>
            <c:otherwise>
                <li class="page-item">
                    <a class="page-link" href="controller?command=show_publications&pageNumber=${currentPage-1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                        <span class="sr-only">Previous</span>
                    </a>
                </li>
            </c:otherwise>
        </c:choose>
        <%--For displaying all available pages--%>
        <c:forEach begin="${currentPage}" end="${(currentPage+3>pageCount) ? pageCount : currentPage+3}" var="i">
            <c:choose>
                <c:when test="${currentPage eq i}">
                    <li class="page-item active">
                        <a class="page-link" href="controller?command=show_publications&pageNumber=${i}">${i}</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link" href="controller?command=show_publications&pageNumber=${i}">${i}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:choose>
            <c:when test="${currentPage eq lastPage}">
                <li class="page-item disabled">
                    <a class="page-link" href="#" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                        <span class="sr-only">Next</span>
                    </a>
                </li>
            </c:when>
            <c:otherwise>
                <li class="page-item">
                    <a class="page-link" href="controller?command=show_publications&pageNumber=${currentPage+1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                        <span class="sr-only">Next</span>
                    </a>
                </li>
            </c:otherwise>
        </c:choose>

    </ul>
</nav>

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
﻿
