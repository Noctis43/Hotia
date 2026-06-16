<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../commun/header.jsp">
    <jsp:param name="titre" value="Demandes"/>
    <jsp:param name="theme" value="app"/>
</jsp:include>

<header class="page-head">
    <h1>Demandes reçues</h1>
    <p class="page-lead">Des personnes souhaitent faire votre connaissance.</p>
</header>

<c:if test="${not empty sessionScope.flashErreur}">
    <div class="alert alert-error" role="alert"><c:out value="${sessionScope.flashErreur}"/></div>
    <c:remove var="flashErreur" scope="session"/>
</c:if>

<section class="card">
    <c:choose>
        <c:when test="${empty demandes}">
            <div class="empty-state">
                <svg class="icon" aria-hidden="true"><use href="#icon-inbox"/></svg>
                <p class="hint">Aucune demande en attente pour le moment.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="demande-list">
                <c:forEach var="item" items="${demandes}">
                    <article class="demande-card">
                        <c:if test="${not empty item.cheminPhoto}">
                            <img src="${pageContext.request.contextPath}/${item.cheminPhoto}"
                                 alt="Photo de ${item.demandeur.prenom}" class="demande-photo" width="108" height="108">
                        </c:if>
                        <div class="demande-body">
                            <h2><c:out value="${item.demandeur.prenom}"/>, <c:out value="${item.age}"/> ans</h2>
                            <p class="ville">
                                <svg class="icon icon--sm" aria-hidden="true"><use href="#icon-location"/></svg>
                                <c:out value="${item.nomVille}"/>
                            </p>
                            <p class="score">
                                <fmt:formatNumber value="${item.score}" maxFractionDigits="0"/> % affinité
                            </p>
                            <div class="demande-actions">
                                <form method="post" action="${pageContext.request.contextPath}/demandes">
                                    <input type="hidden" name="action" value="accepter">
                                    <input type="hidden" name="demandeId" value="${item.demande.id}">
                                    <button type="submit" class="btn btn-primary"
                                        ${statutUtilisateur == 'en_matching' ? 'disabled' : ''}>Accepter</button>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/demandes">
                                    <input type="hidden" name="action" value="refuser">
                                    <input type="hidden" name="demandeId" value="${item.demande.id}">
                                    <button type="submit" class="btn btn-secondary">Refuser</button>
                                </form>
                            </div>
                        </div>
                    </article>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<jsp:include page="../commun/footer.jsp"/>
