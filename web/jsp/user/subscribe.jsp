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

<html lang="en">
<head>
    <title>Genres</title>
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
                <div class="content">
                    <c:choose>
                        <c:when test="${publication!=null}">
                            <form name="subscriptionForm" method="POST" action="controller">
                                <input type="hidden" name="command" value="add_subscription"/>
                                <!-- Publication name-->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="publicationName">Publication name</label>
                                    <div class="col-sm-5">
                                        <input id="publicationName" name="publicationName" type="text"
                                               value="${publication.name}" readonly class="form-control input-md">
                                    </div>
                                </div>

                                <!--Publication type -->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="publicationType">Publication type</label>
                                    <div class="col-sm-5">
                                        <input id="publicationType" name="publicationType" type="text"
                                               value="${publication.publicationType.name}" readonly
                                               class="form-control input-md">
                                    </div>
                                </div>

                                <!-- Publication genre-->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="publicationGenre">Publication
                                        genre</label>
                                    <div class="col-sm-5">
                                        <input id="publicationGenre" name="publicationGenre" type="text"
                                               value="${publication.genre.name}" readonly class="form-control input-md">
                                    </div>
                                </div>

                                <!--Author -->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="authorFirstName">Author first
                                        name</label>
                                    <div class="col-sm-5">
                                        <input id="authorFirstName" name="authorFirstName" type="text"
                                               value="${publication.author.authorFirstName}" readonly
                                               class="form-control input-md">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="authorLastName">Author last name</label>
                                    <div class="col-sm-5">
                                        <input id="authorLastName" name="authorLastName" type="text"
                                               value="${publication.author.authorLastName}" readonly
                                               class="form-control input-md">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="publisherName">Publisher</label>
                                    <div class="col-sm-5">
                                        <input id="publisherName" name="publisherName" type="text"
                                               value="${publication.author.publisherName}" readonly
                                               class="form-control input-md">
                                    </div>
                                </div>

                                <!--Price -->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="price">Price</label>
                                    <div class="col-sm-5">
                                        <input id="price" name="price" type="text" value="${publication.price} BY/month"
                                               readonly class="form-control input-md">
                                    </div>
                                </div>

                                <!--Start date -->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="startDate">Start date</label>
                                    <div class="col-sm-5">
                                        <input id="startDate" name="startDate" type="text" value="${startDate} BY/month"
                                               readonly class="form-control input-md">
                                    </div>
                                </div>

                                <!--End date -->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="endDate">Start date</label>
                                    <div class="col-sm-5">
                                        <input id="endDate" name="endDate" type="text" value="${endDate} BY/month"
                                               readonly class="form-control input-md">
                                    </div>
                                </div>

                                <form method="POST" action="controller">
                                    <input type="hidden" name="command" value="add_subscription"/>
                                    <button class="btn btn-outline-success my-2 my-sm-0" data-toggle="modal"
                                            data-target="#subscribeModal">Subscribe
                                    </button>
                                </form>
                            </form>
                        </c:when>
                    </c:choose>
                </div>
            </div>
            <div class="col-2"></div>
        </div>
    </div>
</div>

<div class="modal fade" id="subscribeModal" tabindex="-1" role="dialog" aria-labelledby="subscribeModal"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <p>Subscription is successfully added.</p>
                <button class="btn btn-primary" type="button" data-dismiss="modal" aria-label="close">Ok</button>
            </div>
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
