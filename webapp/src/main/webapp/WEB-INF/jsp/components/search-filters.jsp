<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="<c:url value="/resources/css/components/search-filters.css"/>" rel="stylesheet" type="text/css"/>
<script src="<c:url value="/resources/js/components/searchFilters.js"/>" type="module"></script>

<!-- Params:
        - url: url to submit the form
-->

<div class="filter-container primary-bg-color">
    <form action="${param.url}" method="post" class="form-style">
        <div class="route-filter">
            <div class="title-row">
                <i class="bi bi-geo-alt-fill light-text h5"></i>
                <h5 class="light-text">Ruta</h5>
            </div>
            <div class="input-container">
                <div class="input-row">
                    <jsp:include page="/WEB-INF/jsp/components/city-selector.jsp">
                        <jsp:param name="id" value="originCity"/>
                        <jsp:param name="defaultText" value="Origen"/>
                    </jsp:include>
                    <div class="route-arrow">
                        <i class="bi bi-arrow-right light-text"></i>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/components/city-selector.jsp">
                        <jsp:param name="id" value="destinationCity"/>
                        <jsp:param name="defaultText" value="Destino"/>
                    </jsp:include>
                </div>
            </div>
        </div>
        <div class="date-filter">
            <div class="title-row">
                <i class="bi bi-calendar light-text h5"></i>
                <h5 class="light-text">Fecha</h5>
            </div>
            <div class="input-container">
                <div class="input-row">
                    <div class="input-group input-group-sm" id="first-date-picker">
                        <button type="button" class="btn button-color">
                            <i class="bi bi-calendar-fill light-text"></i>
                        </button>
                        <input type="text" class="form-control form-control-sm" id="first-date" name="first-date" placeholder="Primera fecha">
                    </div>
                    <div class="input-group input-group-sm" id="time-picker">
                        <button type="button" class="btn button-color">
                            <i class="bi bi-clock-fill light-text"></i>
                        </button>
                        <input type="text" class="form-control form-control-sm" id="time" name="time" placeholder="Hora">
                    </div>
                </div>
                <div class="input-row collapse" id="day-repeat-container">
                    <i class="bi bi-arrow-repeat light-text"></i>
                    <span class="italic-text light-text" id="day-repeat-text"></span>
                </div>
                <div class="input-row">
                    <div class="input-group input-group-sm" id="last-date-picker">
                        <button type="button" class="btn button-color" id="last-date-button" disabled>
                            <i class="bi bi-calendar-fill light-text"></i>
                        </button>
                        <input type="text" class="form-control form-control-sm" id="last-date" name="last-date" placeholder="Última fecha" disabled>

                    </div>
                    <div class="input-group input-group-sm">
                        <div class="input-group-text" id="is-multitrip-container">
                            <input class="form-check-input mt-0" type="checkbox" id="is-multitrip">
                            <span class="mb-0 ml-1 placeholder-text">Viaje recurrente</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="price-filter">
            <div class="title-row">
                <i class="bi bi-currency-dollar light-text h5"></i>
                <h5 class="light-text">Precio</h5>
            </div>
            <div class="input-container">
                <div class="input-row">
                    <div>
                        <input type="number" min="0" class="form-control form-control-sm" id="min-price" name="min-price" placeholder="Mínimo">
                    </div>
                    <span class="light-text h6">-</span>
                    <div>
                        <input type="number" min="0" class="form-control form-control-sm" id="max-price" name="max-price" placeholder="Máximo">
                    </div>
                    <span class="light-text h6">ARS</span>
                </div>
            </div>
        </div>
        <div class="search-button-container">
            <button type="submit" class="btn button-style button-color shadow-btn">
                <i class="bi bi-search light-text h4"></i>
                <span class="button-text-style light-text h4">Buscar</span>
            </button>
        </div>
    </form>
</div>
