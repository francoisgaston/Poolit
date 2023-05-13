<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<link href="<c:url value="/resources/css/components/trip-detail-card.css"/>" rel="stylesheet" type="text/css"/>

<!-- Bean:
        - trip: The information of the trip to show - Trip class
-->

<jsp:useBean id="trip" type="ar.edu.itba.paw.models.trips.Trip" scope="request"/>
<div class="main-container primary-bg-color">
    <div class="details-container">
        <div class="show-row">
            <i class="bi bi-clock light-text"></i>
            <div class="show-row-content">
                <span class="light-text detail"><c:out value="${trip.startTimeString}"/></span>
            </div>
        </div>
        <div class="show-row" >
            <i class="bi bi-calendar light-text h5"></i>
            <div class="show-row-content">
                <span class="light-text detail detail-capitalize"><c:out value="${trip.dayOfWeekString}"/></span>
                <span class="light-text"><spring:message code="tripDetails.card.formatNotRecurrentDate" arguments="${trip.queryStartDateString}"/>
                <c:if test="${trip.queryIsRecurrent}">
                    <spring:message code="tripDetails.card.formatRecurrentDate" arguments="${trip.queryEndDateString}"/>
                </c:if>
                </span>
            </div>
        </div>
        <div class="show-row" >
            <i class="bi bi-car-front-fill light-text h5"></i>
            <div class="show-row-content">
                <span class="light-text detail"><c:out value="${trip.car.infoCar}"/></span>
                <span class="light-text"><spring:message code="profile.plate" arguments="${trip.car.plate}"/></span>
            </div>
        </div>
        <div class="show-row" >
            <i class="bi bi-people light-text h5"></i>
            <div class="show-row-content">
                <span class="light-text detail">
                    <c:choose>
                        <c:when test="${trip.freeSeats==0}"><spring:message code="selectTrip.seats.cero"/></c:when>
                        <c:when test="${trip.freeSeats==1}"><spring:message code="selectTrip.seats.one"/></c:when>
                        <c:otherwise><spring:message code="selectTrip.seats.many" arguments="${trip.freeSeats}"/></c:otherwise>
                    </c:choose>
                </span>
            </div>
        </div>
        <hr/>
        <div class="show-row">
            <c:url value="/image/${trip.driver.userImageId}" var="userImageId"/>
            <c:url value="/profile/${trip.driver.userId}" var="userUrl"/>
            <a href="${userUrl}" class="show-row profile-link">
                <img src="${userImageId}" alt="user image" class="image-photo"/>
                <div class="show-row-content">
                    <span class="light-text detail"><spring:message code="user.nameFormat" arguments="${trip.driver.name}, ${trip.driver.surname}"/></span>
                </div>
            </a>
        </div>
        <div class="show-row">
            <i class="bi bi-envelope-fill light-text h5"></i>
            <div class="show-row-content">
                <span class="light-text detail"><c:out value="${trip.driver.email}"/></span>
            </div>
        </div>
        <div class="show-row">
            <i class="bi bi-telephone-fill light-text h5"></i>
            <div class="show-row-content">
                <span class="light-text detail"><c:out value="${trip.driver.phone}"/></span>
            </div>
        </div>
    </div>
</div>
