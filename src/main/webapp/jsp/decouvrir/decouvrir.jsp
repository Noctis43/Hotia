<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../commun/header.jsp">
    <jsp:param name="titre" value="Découvrir"/>
    <jsp:param name="theme" value="discover"/>
</jsp:include>

<header class="page-head">
    <h1>Découvrir</h1>
    <p class="page-lead">Un profil à la fois — laissez-vous guider par la compatibilité.</p>
</header>

<section class="discover-stage">
    <c:if test="${enMatching}">
        <div class="card empty-state" role="status">
            <svg class="icon" aria-hidden="true"><use href="#icon-link"/></svg>
            <p>Vous êtes en matching. Consultez vos
                <a href="${pageContext.request.contextPath}/chat">messages</a>
                ou la page <a href="${pageContext.request.contextPath}/separation">séparation</a>.
            </p>
        </div>
    </c:if>

    <c:if test="${!enMatching}">
        <c:choose>
            <c:when test="${not empty candidat}">
                <article class="swipe-card" id="swipeCard" data-cible-id="${candidat.utilisateur.id}">
                    <a href="${pageContext.request.contextPath}/profil-detail?id=${candidat.utilisateur.id}" class="card-link">
                        <c:if test="${not empty candidat.photoPrincipale}">
                            <img src="${pageContext.request.contextPath}/${candidat.photoPrincipale.chemin}"
                                 alt="Photo de ${candidat.utilisateur.prenom}" class="card-photo" width="400" height="533">
                        </c:if>
                        <div class="card-info">
                            <h2><c:out value="${candidat.utilisateur.prenom}"/>, <c:out value="${candidat.age}"/> ans</h2>
                            <p class="ville">
                                <svg class="icon icon--sm" aria-hidden="true"><use href="#icon-location"/></svg>
                                <c:out value="${candidat.nomVille}"/>
                            </p>
                            <span class="score-ring">
                                <svg class="icon icon--sm" aria-hidden="true"><use href="#icon-compass"/></svg>
                                <fmt:formatNumber value="${candidat.score}" maxFractionDigits="0"/> % affinité
                            </span>
                        </div>
                    </a>
                </article>

                <div class="swipe-actions">
                    <form method="post" action="${pageContext.request.contextPath}/swipe" class="swipe-form-gauche">
                        <input type="hidden" name="action" value="gauche">
                        <input type="hidden" name="cibleId" value="${candidat.utilisateur.id}">
                        <button type="submit" class="btn-swipe-left" aria-label="Envoyer une demande de match">
                            <svg class="icon" aria-hidden="true"><use href="#icon-heart"/></svg>
                        </button>
                    </form>
                    <form method="post" action="${pageContext.request.contextPath}/swipe" class="swipe-form-droite">
                        <input type="hidden" name="action" value="droite">
                        <input type="hidden" name="cibleId" value="${candidat.utilisateur.id}">
                        <button type="submit" class="btn-swipe-right" aria-label="Passer ce profil">
                            <svg class="icon" aria-hidden="true"><use href="#icon-pass"/></svg>
                        </button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card empty-state">
                    <svg class="icon" aria-hidden="true"><use href="#icon-discover"/></svg>
                    <p>Aucun profil à afficher pour le moment. Revenez plus tard.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </c:if>
</section>

<script src="${pageContext.request.contextPath}/js/swipe.js"></script>
<jsp:include page="../commun/footer.jsp"/>
