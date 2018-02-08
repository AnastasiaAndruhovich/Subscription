<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 25.01.2018
  Time: 14:46
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
<fmt:message bundle="${loc}" key="label.payments" var="Title"/>
<fmt:message bundle="${loc}" key="label.paymentNumber" var="PaymentNumber"/>
<fmt:message bundle="${loc}" key="label.user" var="User"/>
<fmt:message bundle="${loc}" key="label.statement" var="Statement"/>
<fmt:message bundle="${loc}" key="label.sum" var="Sum"/>
<fmt:message bundle="${loc}" key="label.publicationType" var="Type"/>
<fmt:message bundle="${loc}" key="label.author" var="Authors"/>
<fmt:message bundle="${loc}" key="label.publisher" var="Publisher"/>
<fmt:message bundle="${loc}" key="label.price" var="Price"/>
<fmt:message bundle="${loc}" key="label.money" var="Money"/>
<fmt:message bundle="${loc}" key="label.name" var="Name"/>
<fmt:message bundle="${loc}" key="label.authorLastName" var="AuthorLastName"/>
<fmt:message bundle="${loc}" key="label.authorFirstName" var="AuthorFirstName"/>
<fmt:message bundle="${loc}" key="label.date" var="Date"/>
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
    <style><%@include file="../../css/tableStyle.css"%></style>
</head>
<body>
<ctg:role/>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <c:choose>
                    <c:when test="${requestScope.payments!=null||!requestScope.payments.isEmpty()}">
                        <div class="scrolling outer">
                            <div class="inner">
                                <table class="table table-striped table-hover table-condensed table-bordered">
                                    <thead>
                                    <tr>
                                        <th>${PaymentNumber}</th>
                                        <td>${User}</td>
                                        <td>${Name}</td>
                                        <td>${Type}</td>
                                        <td>${Authors}</td>
                                        <td>${Publisher}</td>
                                        <td>${Date}</td>
                                        <td>${Sum} ${Money}</td>
                                        <td>${Statement}</td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="payment" items="${requestScope.payments}">
                                        <tr>
                                            <th>${payment.paymentNumber}</th>
                                            <td>${payment.subscription.user.login}</td>
                                            <td>${payment.subscription.publication.name}</td>
                                            <td>${payment.subscription.publication.publicationType.name}</td>
                                            <td>
                                                <c:if test="${payment.subscription.publication.authors!=null}">
                                                    <c:forEach var="author" items="${payment.subscription.publication.authors}">
                                                        ${author.authorLastName} ${author.authorFirstName}
                                                    </c:forEach>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${payment.subscription.publication.authors!=null}">
                                                    ${payment.subscription.publication.authors[0].publisherName}
                                                </c:if>
                                            </td>
                                            <td>${payment.date}</td>
                                            <td>${payment.sum}</td>
                                            <td>${payment.statement}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p>${InformationIsAbsent}</p>
                    </c:otherwise>
                </c:choose>
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
