<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 26.01.2018
  Time: 17:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtag" %>

<html lang="en">
<head>
    <title>Edit Genre</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">

    <style><%@include file="../../css/style.css" %></style>
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
                        <c:when test="${author!=null}">
                            <form name="addForm" method="POST"
                                  action="${pageContext.servletContext.contextPath}/controller">
                                <input type="hidden" name="command" value="edit_author"/>
                                <input type="hidden" name="authorId" value="${author.authorId}">
                                <p>${result}</p>
                                <div class="form-group row">
                                    <label for="lastName" class="col-sm-2 col-form-label">Author last name</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" name="lastName" id="lastName"
                                               value="${author.authorLastName}"/>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="firstName" class="col-sm-2 col-form-label">Author first name</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" name="firstName" id="firstName"
                                               value="${author.authorFirstName}"/>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="publisherName" class="col-sm-2 col-form-label">Publisher name</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" name="publisherName" id="publisherName"
                                               value="${author.publisherName}" required=""/>
                                    </div>
                                </div>
                                <button class="btn btn-outline-success my-2 my-sm-0">Edit</button>
                            </form>
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