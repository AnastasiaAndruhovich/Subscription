<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 26.01.2018
  Time: 18:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="en">
<head>
    <title>Add Genre</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">

    <style><%@include file="../../css/padding.css"%></style>
</head>
<body>
<%@include file="../../static/admin/header.html" %>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div class="publication card">
                    <form name="addForm" method="POST" action="controller">
                        <input type="hidden" name="command" value="add_publication"/>
                        <div class="form-group row">
                            <label for="name" class="col-sm-2 col-form-label">Name</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="name" id="name"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="publicationType" class="col-sm-2 col-form-label">Publication type</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="publicationType" id="publicationType"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="genre" class="col-sm-2 col-form-label">Genre</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="genre" id="genre"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="lastName" class="col-sm-2 col-form-label">Author last name</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="lastName" id="lastName"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="firstName" class="col-sm-2 col-form-label">Author first name</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="firstName" id="firstName"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="publisherName" class="col-sm-2 col-form-label">Publisher name</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="publisherName" id="publisherName"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="description" class="col-sm-2 col-form-label">Description</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="description" id="description"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="price" class="col-sm-2 col-form-label">Price</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="price" id="price"/>
                            </div>
                        </div>
                        <form action="controller" method="POST" enctype="multipart/form-data">
                            <input type="text" name="description" />
                            <input type="file" name="file" />
                            <button class="btn btn-outline-success my-2 my-sm-0">Add</button>
                        </form>
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