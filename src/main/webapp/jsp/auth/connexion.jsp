<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../commun/header.jsp">
    <jsp:param name="titre" value="Connexion"/>
    <jsp:param name="theme" value="auth"/>
</jsp:include>

<div class="auth-layout">
    <div class="auth-hero">
        <h1>Où les chemins de <em>Madagascar</em> se rencontrent</h1>
        <p>HoTia révèle les affinités qui comptent — proximité, passions partagées, intentions sincères.</p>
        <div class="auth-stats">
            <div class="auth-stat">
                <strong>10</strong>
                <span>Villes</span>
            </div>
            <div class="auth-stat">
                <strong>6</strong>
                <span>Critères</span>
            </div>
        </div>
    </div>

    <section class="card auth-card">
        <h2>Connexion</h2>

        <c:if test="${not empty erreur}">
            <div class="alert alert-error" role="alert"><c:out value="${erreur}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/connexion" class="form-grid">
            <label class="field-label">Email
                <input type="email" name="email" autocomplete="email" required>
            </label>
            <label class="field-label">Mot de passe
                <input type="password" name="motDePasse" autocomplete="current-password" required>
            </label>
            <button type="submit" class="btn btn-primary btn-block">Se connecter</button>
        </form>
        <p class="auth-link">Pas encore de compte ? <a href="${pageContext.request.contextPath}/inscription">Créer un profil</a></p>
    </section>
</div>

<jsp:include page="../commun/footer.jsp"/>
