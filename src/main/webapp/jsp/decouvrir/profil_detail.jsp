<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../commun/header.jsp">
    <jsp:param name="titre" value="Profil"/>
    <jsp:param name="theme" value="app"/>
</jsp:include>

<c:set var="u" value="${profilData.utilisateur}"/>
<c:set var="p" value="${profilData.profil}"/>
<c:set var="desc" value="${profilData.preferenceDescription}"/>

<section class="card profil-detail">
    <div class="back-row">
        <a href="${pageContext.request.contextPath}/decouvrir" class="btn btn-ghost">
            <svg class="icon" aria-hidden="true"><use href="#icon-back"/></svg>
            Retour
        </a>
    </div>

    <div class="galerie">
        <c:forEach var="photo" items="${profilData.photos}">
            <img src="${pageContext.request.contextPath}/${photo.chemin}" alt="Photo de ${u.prenom}">
        </c:forEach>
    </div>

    <header class="profil-header">
        <h1><c:out value="${u.prenom}"/>, <c:out value="${profilData.age}"/> ans</h1>
        <div class="meta-list">
            <span class="meta-chip">
                <svg class="icon icon--sm" aria-hidden="true"><use href="#icon-location"/></svg>
                <c:out value="${profilData.nomVille != null ? profilData.nomVille : 'Non renseignée'}"/>
            </span>
            <span class="meta-chip"><c:out value="${u.sexe}"/></span>
        </div>
        <c:if test="${not empty profilData.scoreCompatibilite}">
            <p class="score-badge">
                <svg class="icon icon--sm" aria-hidden="true"><use href="#icon-compass"/></svg>
                <fmt:formatNumber value="${profilData.scoreCompatibilite}" maxFractionDigits="0"/> % compatible avec vous
            </p>
        </c:if>
    </header>

    <c:if test="${not empty p.bio}">
        <div class="detail-section">
            <h2>Biographie</h2>
            <p><c:out value="${p.bio}"/></p>
        </div>
    </c:if>

    <c:if test="${not empty desc}">
        <div class="detail-section">
            <h2>Divertissements</h2>
            <ul class="tag-list">
                <c:forEach var="d" items="${desc.divertissements}">
                    <li><c:out value="${d}"/></li>
                </c:forEach>
            </ul>
        </div>

        <div class="detail-section">
            <h2>Comportements</h2>
            <ul class="tag-list">
                <c:forEach var="cpt" items="${desc.comportements}">
                    <li><c:out value="${cpt}"/></li>
                </c:forEach>
            </ul>
        </div>

        <div class="detail-section">
            <p><strong>Situation familiale :</strong> <c:out value="${desc.situationFamiliale}"/></p>
            <p><strong>Type de relation :</strong> <c:out value="${desc.typeRelation}"/></p>
        </div>
    </c:if>
</section>

<jsp:include page="../commun/footer.jsp"/>
