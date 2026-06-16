<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../commun/header.jsp">
    <jsp:param name="titre" value="Séparation"/>
    <jsp:param name="theme" value="app"/>
</jsp:include>

<header class="page-head">
    <h1>Séparation</h1>
</header>

<section class="card separation-panel">
    <p class="separation-intro">Vous êtes en matching avec <strong><c:out value="${prenomPartenaire}"/></strong>.</p>

    <c:if test="${not empty sessionScope.flashErreur}">
        <div class="alert alert-error" role="alert"><c:out value="${sessionScope.flashErreur}"/></div>
        <c:remove var="flashErreur" scope="session"/>
    </c:if>

    <c:choose>
        <c:when test="${empty separation}">
            <p>Souhaitez-vous mettre fin à ce matching ? Cette action nécessitera la confirmation de votre partenaire.</p>
            <form method="post" action="${pageContext.request.contextPath}/separation">
                <input type="hidden" name="action" value="demander">
                <button type="submit" class="btn btn-danger">Demander une séparation</button>
            </form>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${separation.demandeurId == sessionScope.utilisateurConnecte.id}">
                    <div class="alert alert-info" role="status">
                        Votre demande est en attente de confirmation par votre partenaire.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-warning" role="alert">
                        Votre partenaire a demandé une séparation.
                    </div>
                    <div class="separation-actions">
                        <form method="post" action="${pageContext.request.contextPath}/separation">
                            <input type="hidden" name="action" value="confirmer">
                            <input type="hidden" name="separationId" value="${separation.id}">
                            <button type="submit" class="btn btn-danger">Confirmer la séparation</button>
                        </form>
                        <form method="post" action="${pageContext.request.contextPath}/separation">
                            <input type="hidden" name="action" value="refuser">
                            <input type="hidden" name="separationId" value="${separation.id}">
                            <button type="submit" class="btn btn-secondary">Refuser</button>
                        </form>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
</section>

<jsp:include page="../commun/footer.jsp"/>
