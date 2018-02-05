<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 04.02.2018
  Time: 23:49
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
<fmt:message bundle="${loc}" key="label.users" var="Title"/>
<fmt:message bundle="${loc}" key="label.role" var="Role"/>
<fmt:message bundle="${loc}" key="label.login" var="Login"/>
<fmt:message bundle="${loc}" key="label.userLastName" var="LastName"/>
<fmt:message bundle="${loc}" key="label.userFirstName" var="FirstName"/>
<fmt:message bundle="${loc}" key="label.address" var="Address"/>
<fmt:message bundle="${loc}" key="label.city" var="City"/>
<fmt:message bundle="${loc}" key="label.postalIndex" var="PostalIndex"/>
<fmt:message bundle="${loc}" key="label.balance" var="Balance"/>
<fmt:message bundle="${loc}" key="label.loan" var="Loan"/>
<fmt:message bundle="${loc}" key="label.money" var="Money"/>
<fmt:message bundle="${loc}" key="label.subscriptions" var="Subscriptions"/>
<fmt:message bundle="${loc}" key="label.payments" var="Payments"/>
<fmt:message bundle="${loc}" key="label.block" var="Block"/>
<fmt:message bundle="${loc}" key="label.blockedUsers" var="BlockedUsers"/>
<fmt:message bundle="${loc}" key="label.detailed" var="Detailed"/>
<fmt:message bundle="${loc}" key="button.block" var="BlockButton"/>
<fmt:message bundle="${loc}" key="button.unblock" var="UnblockButton"/>
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
                <div class="scrolling outer">
                    <div class="inner">
                        <table class="table table-striped table-hover table-condensed table-bordered">
                        <thead>
                            <tr>
                                <th>#</th>
                                <td>${Role}</td>
                                <td>${Login}</td>
                                <td>${LastName}</td>
                                <td>${FirstName}</td>
                                <td>${Address}</td>
                                <td>${City}</td>
                                <td>${PostalIndex}</td>
                                <td>${Balance} ${Money}</td>
                                <td>${Loan} ${Money}</td>
                                <td>${Subscriptions}</td>
                                <td>${Payments}</td>
                                <td>${Block}</td>
                                <td>${BlockedUsers}</td>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${requestScope.users!=null}">
                                    <c:forEach var="user" items="${requestScope.users}">
                                        ${requestScope.result}
                                        <tr>
                                            <th>1</th>
                                            <td>${user.role.name}</td>
                                            <td>${user.login}</td>
                                            <td>${user.lastName}</td>
                                            <td>${user.firstName}</td>
                                            <td>${user.address}</td>
                                            <td>${user.city}</td>
                                            <td>${user.postalIndex}</td>
                                            <td>${user.account.balance}</td>
                                            <td>${user.account.loan}</td>
                                            <td>
                                                <a href="${pageContext.servletContext.contextPath}/controller?command=show_subscriptions_by_user_id&userId=${user.userId}">${Detailed}</a>
                                            </td>
                                            <td>
                                                <a href="${pageContext.servletContext.contextPath}/controller?command=show_payments_by_user_id&userId=${user.userId}">${Detailed}</a>
                                            </td>
                                            <td>${user.admin.login}
                                                <c:if test="${user.userId!=2}">
                                                    <c:choose>
                                                        <c:when test="${user.admin==null}">
                                                            <form method="POST" action="${pageContext.servletContext.contextPath}/controller">
                                                                <input type="hidden" name="command" value="block_user"/>
                                                                <input type="hidden" name="userId" value="${user.userId}">
                                                                <input type="hidden" name="adminId" value="${sessionScope.clientId}">
                                                                <button class="btn btn-outline-danger my-2 my-sm-0">${BlockButton}</button>
                                                            </form>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <form method="POST" action="${pageContext.servletContext.contextPath}/controller">
                                                                <input type="hidden" name="command" value="unblock_user"/>
                                                                <input type="hidden" name="userId" value="${user.userId}">
                                                                <button class="btn btn-outline-danger my-2 my-sm-0">${UnblockButton}</button>
                                                            </form>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${user != null && !user.users.isEmpty()}">
                                                    <a href="${pageContext.servletContext.contextPath}/controller?command=find_blocked_users_by_admin_id&adminId=${sessionScope.clientId}">${Detailed}</a>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <p>${InformationIsAbsent}</p>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
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
