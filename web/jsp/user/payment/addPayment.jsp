<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 25.01.2018
  Time: 11:36
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
<fmt:message bundle="${loc}" key="label.paySubscription" var="Title"/>
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
<fmt:message bundle="${loc}" key="label.startDate" var="StartDate"/>
<fmt:message bundle="${loc}" key="label.endDate" var="EndDate"/>
<fmt:message bundle="${loc}" key="label.active" var="Active"/>
<fmt:message bundle="${loc}" key="button.payNow" var="PayNow"/>
<fmt:message bundle="${loc}" key="button.payLater" var="PayLater"/>
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

    <jsp:useBean id="subscription" scope="session" type="by.andruhovich.subscription.entity.Subscription"/>
    <style><%@include file="../../../css/style.css"%></style>

</head>
<body>
<ctg:role/>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div class="publication card">
                    ${sessionScope.message}
                    <div class="row">
                        <div class="col-4">
                            <c:if test="${subscription!=null}">
                                <ctg:image publicationId="${subscription.publication.publicationId}"/>
                            </c:if>
                        </div>
                        <div class="col-8">
                            <div class="publication card">
                                <c:choose>
                                    <c:when test="${subscription!=null}">
                                        <p>${requestScope.successfulAddPublication}</p>
                                        <p>${Name}: ${subscription.publication.name}</p>
                                        <p>${Type}: ${subscription.publication.publicationType.name}</p>
                                        <p>${Genre}: ${subscription.publication.genre.name}</p>
                                        <c:choose>
                                            <c:when test="${subscription.publication.authors!=null}">
                                                <div class="row">${Authors}:
                                                    <c:forEach var="author" items="${subscription.publication.authors}">
                                                        ${author.authorLastName} ${author.authorFirstName}
                                                    </c:forEach>
                                                </div>
                                                <p>${Publisher}: ${subscription.publication.authors[0].publisherName}</p>
                                            </c:when>
                                        </c:choose>
                                        <p>${Description}: ${subscription.publication.description}</p>
                                        <p>${StartDate}: ${subscription.startDate}</p>
                                        <p>${EndDate}: ${subscription.endDate}</p>
                                        <p>${Active}: ${subscription.subscriptionIsActive}</p>
                                        <p>${Price}: ${subscription.publication.price} ${MoneyUnit}</p>
                                        <c:if test="${!subscription.subscriptionIsActive}">
                                            <div class="row">
                                                <form method="POST" action="${pageContext.servletContext.contextPath}/controller">
                                                    <input type="hidden" name="command" value="add_payment"/>
                                                    <input type="hidden" name="subscriptionId" value="${subscription.subscriptionId}">
                                                    <button class="btn btn-outline-success my-2 my-sm-0">${PayNow}</button>
                                                </form>
                                                <form method="POST" action="${pageContext.servletContext.contextPath}/controller">
                                                    <input type="hidden" name="command" value="show_publications"/>
                                                    <button class="btn btn-outline-warning my-2 my-sm-0">${PayLater}</button>
                                                </form>
                                                <form method="POST" action="${pageContext.servletContext.contextPath}/controller">
                                                    <input type="hidden" name="command" value="delete_subscription"/>
                                                    <input type="hidden" name="subscriptionId" value="${subscription.subscriptionId}"/>
                                                    <button class="btn btn-outline-danger my-2 my-sm-0">${Delete}</button>
                                                </form>
                                            </div>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <p>${InformationIsAbsent}</p>
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

<%@include file="../../../static/common/footer.html" %>

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
