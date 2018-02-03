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

<html lang="en">
<head>
    <title>Authors</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
</head>
<body>
<ctg:role/>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div class="author card">
                    <c:choose>
                        <c:when test="${authors!=null}">
                            <c:forEach var="author" items="${authors}">
                                <p>
                                    <a href="${pageContext.servletContext.contextPath}/controller?command=find_publications_by_author&authorId=${author.authorId}"> ${author.authorLastName} ${author.authorFirstName} ${author.publisherName}</a>
                                </p>
                                <div class="row">
                                    <form method="GET" action="${pageContext.servletContext.contextPath}/controller">
                                        <input type="hidden" name="command" value="redirect_update_author"/>
                                        <button class="btn btn-outline-warning my-2 my-sm-0">Edit</button>
                                    </form>
                                    <button class="btn btn-outline-danger my-2 my-sm-0" data-target="#authorModal" data-toggle="modal">Delete</button>
                                    <div class="modal fade" id="authorModal" tabindex="-1" role="dialog" aria-labelledby="authorModal" aria-hidden="true">
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
                                                        <input type="hidden" name="command" value="delete_author"/>
                                                        <input type="hidden" name="authorId" value="${author.authorId}">
                                                        <button class="btn btn-outline-danger my-2 my-sm-0">Delete</button>
                                                    </form>
                                                </div>
                                            </div>
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
