<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 25.01.2018
  Time: 14:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="en">
<head>
    <title>Profile</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
</head>
<body>
<%@include file="../../static/user/header.jsp" %>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-2"></div>
            <div class="col-8">
                <c:choose>
                    <c:when test="${user!=null}">
                        <form name="profileForm" method="POST" action="controller">
                            <input type="hidden" name="command" value="update_user"/>

                            <!-- User names-->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="lastName">Last Name</label>
                                <div class="col-sm-9">
                                    <input id="lastName" name="lastName" type="text" value="${user.lastName}"
                                           class="form-control input-md"
                                           required="">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="firstName">First Name</label>
                                <div class="col-sm-9">
                                    <input id="firstName" name="firstName" type="text" value="${user.firstName}"
                                           class="form-control input-md"
                                           required="">
                                </div>
                            </div>

                            <!--Login -->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="registerLogin">Login</label>
                                <div class="col-sm-9">
                                    <input id="registerLogin" name="login" type="text" value="${user.login}"
                                           class="form-control input-md"
                                           required="">
                                </div>
                            </div>

                            <!--Old Password -->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="oldPassword">Old Password</label>
                                <div class="col-sm-9">
                                    <input id="oldPassword" name="oldPassword" type="password" placeholder=" Old password"
                                           class="form-control input-md" required="">
                                </div>
                            </div>

                            <!--New Password -->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="newPassword">New Password</label>
                                <div class="col-sm-9">
                                    <input id="newPassword" name="newPassword" type="password" placeholder="New password"
                                           class="form-control input-md" required="">
                                </div>
                            </div>

                            <!--Duplicated Password -->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="duplicatedPassword">Repeat password</label>
                                <div class="col-sm-9">
                                    <input id="duplicatedPassword" name="duplicatedPassword" type="password"
                                           placeholder="Repeat password"
                                           class="form-control input-md" required="">
                                </div>
                            </div>

                            <!-- Select Date Of Birth-->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="month">Month</label>

                                <div class="col-sm-3">
                                    <select id="month" name="month" class="form-control">
                                        <option selected value="default">${month}</option>
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                        <option value="6">6</option>
                                        <option value="7">7</option>
                                        <option value="8">8</option>
                                        <option value="9">9</option>
                                        <option value="10">10</option>
                                        <option value="11">11</option>
                                        <option value="12">12</option>
                                    </select>
                                </div>


                                <label class="col-sm-2 control-label" for="day">Day</label>
                                <div class="col-sm-3">
                                    <select id="day" name="day" class="form-control">
                                        <option selected value="default">${day}</option>
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                        <option value="6">6</option>
                                        <option value="7">7</option>
                                        <option value="8">8</option>
                                        <option value="9">9</option>
                                        <option value="10">10</option>
                                        <option value="11">11</option>
                                        <option value="12">12</option>
                                        <option value="13">13</option>
                                        <option value="14">14</option>
                                        <option value="15">15</option>
                                        <option value="16">16</option>
                                        <option value="17">17</option>
                                        <option value="18">18</option>
                                        <option value="19">19</option>
                                        <option value="20">20</option>
                                        <option value="21">21</option>
                                        <option value="22">22</option>
                                        <option value="23">23</option>
                                        <option value="24">24</option>
                                        <option value="25">25</option>
                                        <option value="26">26</option>
                                        <option value="27">27</option>
                                        <option value="28">28</option>
                                        <option value="29">29</option>
                                        <option value="30">30</option>
                                        <option value="31">31</option>
                                    </select>
                                </div>

                                <label class="col-sm-3 control-label" for="year">Year</label>
                                <div class="col-sm-3">
                                    <select id="year" name="year" class="form-control">
                                        <option selected value="default">${year}</option>
                                        <option value="1990">1990</option>
                                        <option value="1991">1991</option>
                                        <option value="1992">1992</option>
                                        <option value="1993">1993</option>
                                        <option value="1994">1994</option>
                                        <option value="1995">1995</option>
                                        <option value="1996">1996</option>
                                        <option value="1997">1997</option>
                                        <option value="1998">1998</option>
                                        <option value="2000">2000</option>
                                        <option value="2001">2001</option>
                                        <option value="2002">2002</option>
                                        <option value="2003">2003</option>
                                        <option value="2004">2004</option>
                                        <option value="2005">2005</option>
                                        <option value="2006">2006</option>
                                        <option value="2007">2007</option>
                                        <option value="2008">2008</option>
                                        <option value="2009">2009</option>
                                        <option value="2010">2010</option>
                                        <option value="2011">2011</option>
                                        <option value="2012">2012</option>
                                        <option value="2013">2013</option>
                                        <option value="2014">2014</option>
                                        <option value="2015">2015</option>
                                        <option value="2016">2016</option>
                                        <option value="2017">2017</option>
                                        <option value="2018">2018</option>
                                    </select>
                                </div>
                            </div>

                            <!-- Address input-->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="address">Address</label>
                                <div class="col-sm-9">
                                    <input id="address" name="address" type="text" value="${user.address}"
                                           class="form-control input-md"
                                           required="">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="city">City</label>
                                <div class="col-sm-9">
                                    <input id="city" name="city" type="text" value="${user.city}" class="form-control input-md"
                                           required="">
                                </div>
                            </div>

                            <!-- Postal index input-->
                            <div class="form-group row">
                                <label class="col-sm-3 control-label" for="postalIndex">Postal Index</label>
                                <div class="col-sm-9">
                                    <input id="postalIndex" name="postalIndex" type="number" value="${user.postalIndex}"
                                           class="form-control input-md" required="">
                                </div>
                            </div>

                            <div class="modal-footer">
                                <button class="btn btn-primary">Save</button>
                            </div>
                        </form>
                    </c:when>
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
