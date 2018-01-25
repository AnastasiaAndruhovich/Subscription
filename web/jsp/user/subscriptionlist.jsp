<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 25.01.2018
  Time: 11:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="en">
<head>
    <title>Publication Types</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
</head>
<body>
<%@include file="../../static/user/header.html" %>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-2"></div>
            <div class="col-8">
                <c:choose>
                    <c:when test="${subscriptions!=null}">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th scope="col">Publication Name</th>
                                <th scope="col">Publication Type</th>
                                <th scope="col">Genre</th>
                                <th scope="col">Authors</th>
                                <th scope="col">Publisher</th>
                                <th scope="col">Start date</th>
                                <th scope="col">End date</th>
                                <th scope="col">Active</th>
                                <th scope="col">Payment</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <c:forEach var="subscription" items="${subscriptions}">
                                    <td>${subscription.publication.name}</td>
                                    <td>${subscription.publication.publicationType.name}</td>
                                    <td>${subscription.publication.genre.name}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="subscription.publication.authors!=null">
                                                <c:forEach var="author" items="subsctription.publication.authors">
                                                    ${author.authorFirstName} ${author.authorLastName}
                                                </c:forEach>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="subscription.publication.authors!=null">
                                                ${subscription.publication.authors[0].publisherName}
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>${startDate}</td>
                                    <td>${endDate}</td>
                                    <td>${subsctription.isActive}</td>
                                    <td>
                                        <form method="POST" action="controller">
                                            <input type="hidden" name="command" value="pay_subscription"/>
                                            <input type="hidden" name="subscriptionId"
                                                   value="${subscription.subscriptionId}">
                                            <button class="btn btn-outline-success my-2 my-sm-0">Pay</button>
                                        </form>
                                    </td>
                                </c:forEach>
                            </tr>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p>${infromationIsAbsent}</p>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col-2"></div>
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