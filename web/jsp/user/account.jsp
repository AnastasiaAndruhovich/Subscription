<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 25.01.2018
  Time: 14:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtag" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="locale.locale" var="loc"/>
<fmt:message bundle="${loc}" key="label.account" var="Title"/>
<fmt:message bundle="${loc}" key="label.balance" var="Balance"/>
<fmt:message bundle="${loc}" key="label.loan" var="Loan"/>
<fmt:message bundle="${loc}" key="label.money" var="Money"/>
<fmt:message bundle="${loc}" key="label.loanSum" var="LoanSum"/>
<fmt:message bundle="${loc}" key="label.rechargeSum" var="RechargeSum"/>
<fmt:message bundle="${loc}" key="button.recharge" var="Recharge"/>
<fmt:message bundle="${loc}" key="button.takeLoan" var="TakeLoan"/>
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

    <jsp:useBean id="account" scope="request" type="by.andruhovich.subscription.entity.Account"/>
    <style>
        <%@include file="../../css/style.css" %>
    </style>
</head>
<body>
<ctg:role/>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                ${requestScope.errorSum}
                <c:choose>
                    <c:when test="${account!=null}">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th scope="col">${Balance} ${Money}</th>
                                <th scope="col">${Loan} ${Money}</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>${account.balance}</td>
                                <td>${account.loan}</td>
                            </tr>
                            </tbody>
                        </table>

                        <!--Recharge -->
                        <form method="POST" action="${pageContext.servletContext.contextPath}/controller">
                            <input type="hidden" name="command" value="recharge"/>
                            <div class="form-group row">
                                <label class="col-sm-2 control-label" for="rechargeSum">${RechargeSum}</label>
                                <div class="col-sm-3">
                                    <input id="rechargeSum" name="rechargeSum" type="text" placeholder="0.00"
                                           class="form-control input-md" pattern="^[\d]+?\.[\d]{2}$"/>
                                </div>
                                <div class="col-sm-3">
                                    <button class="btn btn-outline-success my-2 my-sm-0">${Recharge}</button>
                                </div>
                            </div>
                        </form>

                        <!--Take a loan -->
                        <form method="POST" action="${pageContext.servletContext.contextPath}/controller">
                            <input type="hidden" name="command" value="take_loan"/>
                            <div class="form-group row">
                                <label class="col-sm-2 control-label" for="loanSum">${LoanSum}</label>
                                <div class="col-sm-3">
                                    <input id="loanSum" name="loanSum" type="text" placeholder="0.00"
                                           class="form-control input-md" pattern="^[\d]+?\.[\d]{2}$"/>
                                </div>
                                <div class="col-sm-3">
                                    <button class="btn btn-outline-warning my-2 my-sm-0">${TakeLoan}</button>
                                </div>
                            </div>
                        </form>
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
