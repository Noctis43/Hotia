<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../commun/header.jsp">
    <jsp:param name="titre" value="Inscription"/>
    <jsp:param name="theme" value="auth"/>
</jsp:include>

<div class="auth-layout">
    <div class="auth-hero">
        <h1>Rejoignez la <em>communauté</em> HoTia</h1>
        <p>Créez votre espace en quelques minutes. Chaque profil est pensé pour des rencontres authentiques, ancrées dans l'île.</p>
        <div class="auth-stats">
            <div class="auth-stat">
                <strong>18+</strong>
                <span>Âge minimum</span>
            </div>
           
        </div>
    </div>

    <section class="card auth-card">
        <h2>Créer un compte</h2>

        <c:if test="${not empty erreurs}">
            <div class="alert alert-error" role="alert">
                <ul>
                    <c:forEach var="msg" items="${erreurs}">
                        <li><c:out value="${msg}"/></li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/inscription" class="form-grid">
            <label class="field-label">Nom
                <input type="text" name="nom" value="<c:out value='${nom}'/>" required>
            </label>
            <label class="field-label">Prénom
                <input type="text" name="prenom" value="<c:out value='${prenom}'/>" required>
            </label>
            <label class="field-label">Email
                <input type="email" name="email" value="<c:out value='${email}'/>" autocomplete="email" required>
            </label>
            <label class="field-label">Date de naissance
                <input type="date" name="dateNaissance" value="<c:out value='${dateNaissance}'/>" required>
            </label>
            <label class="field-label">Sexe
                <select name="sexe" required>
                    <option value="">Choisir</option>
                    <option value="Homme" ${sexe == 'Homme' ? 'selected' : ''}>Homme</option>
                    <option value="Femme" ${sexe == 'Femme' ? 'selected' : ''}>Femme</option>
                    <option value="Autre" ${sexe == 'Autre' ? 'selected' : ''}>Autre</option>
                </select>
            </label>
            <label class="field-label">Mot de passe (min. 8 caractères)
                <input type="password" name="motDePasse" autocomplete="new-password" required minlength="8">
            </label>
            <label class="field-label">Confirmer le mot de passe
                <input type="password" name="confirmerMotDePasse" autocomplete="new-password" required minlength="8">
            </label>
            <button type="submit" class="btn btn-primary btn-block">S'inscrire</button>
        </form>
        <p class="auth-link">Déjà inscrit ? <a href="${pageContext.request.contextPath}/connexion">Se connecter</a></p>
    </section>
</div>

<jsp:include page="../commun/footer.jsp"/>
