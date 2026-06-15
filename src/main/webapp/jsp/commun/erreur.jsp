<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp">
    <jsp:param name="titre" value="Erreur"/>
    <jsp:param name="theme" value="auth"/>
</jsp:include>

<section class="card erreur-card">
    <svg class="icon" aria-hidden="true" style="width:3rem;height:3rem;margin:0 auto 1rem;color:var(--accent-coral)"><use href="#icon-alert"/></svg>
    <h1>Une erreur est survenue</h1>
    <p class="alert alert-error" role="alert"><c:out value="${erreur}"/></p>
    <a href="${pageContext.request.contextPath}/connexion" class="btn btn-primary">Retour à l'accueil</a>
</section>

<jsp:include page="footer.jsp"/>