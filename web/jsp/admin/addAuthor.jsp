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
<%@ taglib prefix="ctg" uri="customtag"%>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="locale.locale" var="loc"/>
<fmt:message bundle="${loc}" key="label.addAuthor" var="Title"></fmt:message>
<fmt:message bundle="${loc}" key="label.publisher" var="Publisher"></fmt:message>
<fmt:message bundle="${loc}" key="button.add" var="Add"></fmt:message>
<fmt:message bundle="${loc}" key="label.authorLastName" var="AuthorLastName"></fmt:message>
<fmt:message bundle="${loc}" key="label.authorFirstName" var="AuthorFirstName"></fmt:message>

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
<ctg:role></ctg:role>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div class="author card">
                    <form name="addForm" method="POST" action="../../controller">
                        <input type="hidden" name="command" value="add_author"/>
                        <div class="form-group row">
                            <label for="lastName" class="col-sm-2 col-form-label">${AuthorLastName}</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="lastName" id="lastName" required=""
                                       placeholder="Last name"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="firstName" class="col-sm-2 col-form-label">${AuthorFirstName}</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="firstName" id="firstName" required=""
                                       placeholder="First name"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="publisherName" class="col-sm-2 col-form-label">${Publisher}</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="publisherName" id="publisherName"
                                       placeholder="Publisher name" required=""/>
                            </div>
                        </div>
                        <button class="btn btn-outline-success my-2 my-sm-0">${Add}</button>
                    </form>
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
