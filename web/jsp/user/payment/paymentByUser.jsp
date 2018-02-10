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

    <style><%@include file="../../../css/style.css"%></style>
    <style><%@include file="../../../css/tableStyle.css"%></style>
</head>
<body>
<ctg:role/>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <c:choose>
                    <c:when test="${sessionScope.payments!=null && !sessionScope.payments.isEmpty()}">
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
                                    <c:forEach var="payment" items="${sessionScope.payments}">
                                        <tr>
                                            <th>${payment.paymentNumber}</th>
                                            <td>${payment.subscription.user.login}</td>
                                            <td>${payment.subscription.publication.name}</td>
                                            <td>
                                                <a href="${pageContext.servletContext.contextPath}/controller?command=find_publication_by_publication_type&publicationTypeId=${payment.subscription.publication.publicationType.publicationTypeId}">${payment.subscription.publication.publicationType.name}</a>
                                            </td>
                                            <td>
                                                <c:if test="${payment.subscription.publication.authors!=null}">
                                                    <c:forEach var="author" items="${payment.subscription.publication.authors}">
                                                        <a href="${pageContext.servletContext.contextPath}/controller?command=find_publication_by_author&authorId=${author.authorId}&pageNumber=1">${author.authorLastName} ${author.authorFirstName}</a>
                                                    </c:forEach>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${payment.subscription.publication.authors!=null}">
                                                    <a href="${pageContext.servletContext.contextPath}/controller?command=find_publication_by_author&authorId=${payment.subscription.publication.authors[0].authorId}&pageNumber=1">${payment.subscription.publication.authors[0].publisherName}</a>
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

<%--Declare variable for current page and count of contacts --%>
<c:set var="currentPage" value="${(sessionScope.pageNumber==null) ? 1 : sessionScope.pageNumber}"/>
<c:set var="lastPage" value="${(sessionScope.pageCount==null) ? 1 : sessionScope.pageCount}"/>

<c:if test="${sessionScope.payments!=null && !sessionScope.payments.isEmpty()}">
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
                        <a class="page-link" href="${pageContext.servletContext.contextPath}/controller?command=find_payment_by_user&userId=${sessionScope.clientId}&pageNumber=${currentPage-1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                            <span class="sr-only">Previous</span>
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
                <%--For displaying all available pages--%>
            <c:forEach begin="${(currentPage-1<1)?1:currentPage-1}" end="${(currentPage+1>lastPage)?lastPage:currentPage+1}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <li class="page-item active">
                            <a class="page-link" href="${pageContext.servletContext.contextPath}/controller?command=find_payment_by_user&userId=${sessionScope.clientId}&pageNumber=${i}">${i}</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link" href="${pageContext.servletContext.contextPath}/controller?command=find_payment_by_user&userId=${sessionScope.clientId}&pageNumber=${i}">${i}</a>
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
                        <a class="page-link" href="${pageContext.servletContext.contextPath}/controller?command=find_payment_by_user&userId=${sessionScope.clientId}&pageNumber=${currentPage+1}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                            <span class="sr-only">Next</span>
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</c:if>

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
