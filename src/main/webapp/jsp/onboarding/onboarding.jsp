<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../commun/header.jsp">
    <jsp:param name="titre" value="Onboarding"/>
    <jsp:param name="theme" value="form"/>
</jsp:include>

<header class="page-head">
    <h1>Complétez votre profil</h1>
    <p class="page-lead">Quelques étapes pour révéler qui vous êtes et ce que vous recherchez.</p>
</header>

<section class="card form-large">
    <c:if test="${not empty erreurs}">
        <div class="alert alert-error" role="alert"><ul>
            <c:forEach var="msg" items="${erreurs}">
                <li><c:out value="${msg}"/></li>
            </c:forEach>
        </ul></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/onboarding"
          enctype="multipart/form-data" class="form-sections">

        <fieldset>
            <legend>Photo de profil</legend>
            <div class="file-drop">
                <input type="file" name="photo" accept="image/*" required aria-label="Choisir une photo de profil">
                <p class="hint">Portrait net, visage visible — formats image acceptés.</p>
            </div>
        </fieldset>

        <fieldset>
            <legend>Téléphone</legend>
            <input type="tel" name="telephone" placeholder="03X XX XXX XX" pattern="03[0-9] [0-9]{2} [0-9]{3} [0-9]{2}" required autocomplete="tel">
        </fieldset>

        <fieldset>
            <legend>Biographie (optionnel, max 500 car.)</legend>
            <textarea name="bio" maxlength="500" rows="4"></textarea>
        </fieldset>

        <fieldset>
            <legend>Localisation</legend>
            <label>Ville
                <select name="villeId" id="villeId">
                    <option value="">-- Choisir une ville --</option>
                    <c:forEach var="ville" items="${villes}">
                        <option value="${ville.id}"><c:out value="${ville.nom}"/></option>
                    </c:forEach>
                </select>
            </label>
            <p class="hint">Ou positionnez-vous via le GPS :</p>
            <button type="button" id="btnGps" class="btn btn-secondary">
                <svg class="icon" aria-hidden="true"><use href="#icon-gps"/></svg>
                Activer le GPS
            </button>
            <input type="hidden" name="latitude" id="latitude">
            <input type="hidden" name="longitude" id="longitude">
            <p id="gpsStatus" class="hint"></p>
        </fieldset>

        <fieldset>
            <legend>Préférences de recherche</legend>
            <p class="subsection-title">Divertissements recherchés</p>
            <div class="checkbox-grid">
                <c:forEach var="entry" items="${divertissementsRef}">
                    <label><input type="checkbox" name="divertissementsRecherche" value="${entry.key}"> <c:out value="${entry.value}"/></label>
                </c:forEach>
            </div>
            <p class="subsection-title">Comportements recherchés</p>
            <div class="checkbox-grid">
                <c:forEach var="entry" items="${comportementsRef}">
                    <label><input type="checkbox" name="comportementsRecherche" value="${entry.key}"> <c:out value="${entry.value}"/></label>
                </c:forEach>
            </div>
            <label>Sexe recherché
                <select name="sexeCibleRecherche">
                    <option value="Peu importe">Peu importe</option>
                    <option value="Homme">Homme</option>
                    <option value="Femme">Femme</option>
                    <option value="Autre">Autre</option>
                </select>
            </label>
            <label>Situation familiale acceptée
                <select name="situationRecherche">
                    <option value="Peu importe">Peu importe</option>
                    <option value="Célibataire">Célibataire</option>
                    <option value="Divorcé">Divorcé</option>
                    <option value="Veuf">Veuf</option>
                </select>
            </label>
            <label>Type de relation
                <select name="typeRelationRecherche">
                    <option value="Peu importe">Peu importe</option>
                    <option value="Relation sérieuse">Relation sérieuse</option>
                    <option value="Amitié">Amitié</option>
                    <option value="Aventure">Aventure</option>
                </select>
            </label>
            <div class="row-2">
                <label>Âge min <input type="number" name="ageMinRecherche" min="18" max="99"></label>
                <label>Âge max <input type="number" name="ageMaxRecherche" min="18" max="99"></label>
            </div>
        </fieldset>

        <fieldset>
            <legend>Description de vous-même</legend>
            <p class="subsection-title">Divertissements pratiqués</p>
            <div class="checkbox-grid">
                <c:forEach var="entry" items="${divertissementsRef}">
                    <label><input type="checkbox" name="divertissementsDescription" value="${entry.key}"> <c:out value="${entry.value}"/></label>
                </c:forEach>
            </div>
            <p class="subsection-title">Comportements qui me décrivent</p>
            <div class="checkbox-grid">
                <c:forEach var="entry" items="${comportementsRef}">
                    <label><input type="checkbox" name="comportementsDescription" value="${entry.key}"> <c:out value="${entry.value}"/></label>
                </c:forEach>
            </div>
            <label>Mon sexe (description)
                <select name="sexeCibleDescription">
                    <option value="Homme" ${sessionScope.utilisateurConnecte.sexe == 'Homme' ? 'selected' : ''}>Homme</option>
                    <option value="Femme" ${sessionScope.utilisateurConnecte.sexe == 'Femme' ? 'selected' : ''}>Femme</option>
                    <option value="Autre" ${sessionScope.utilisateurConnecte.sexe == 'Autre' ? 'selected' : ''}>Autre</option>
                </select>
            </label>
            <label>Ma situation familiale
                <select name="situationDescription">
                    <option value="Célibataire">Célibataire</option>
                    <option value="Divorcé">Divorcé</option>
                    <option value="Veuf">Veuf</option>
                </select>
            </label>
            <label>Type de relation souhaité
                <select name="typeRelationDescription">
                    <option value="Relation sérieuse">Relation sérieuse</option>
                    <option value="Amitié">Amitié</option>
                    <option value="Aventure">Aventure</option>
                </select>
            </label>
            <div class="row-2">
                <label>Âge min <input type="number" name="ageMinDescription" min="18" max="99"></label>
                <label>Âge max <input type="number" name="ageMaxDescription" min="18" max="99"></label>
            </div>
        </fieldset>

        <button type="submit" class="btn btn-primary btn-block">Terminer l'onboarding</button>
    </form>
</section>

<script>
document.getElementById('btnGps').addEventListener('click', function() {
    var status = document.getElementById('gpsStatus');
    if (!navigator.geolocation) {
        status.textContent = 'GPS non supporté par ce navigateur.';
        return;
    }
    navigator.geolocation.getCurrentPosition(function(pos) {
        document.getElementById('latitude').value = pos.coords.latitude;
        document.getElementById('longitude').value = pos.coords.longitude;
        document.getElementById('villeId').value = '';
        status.textContent = 'Position GPS enregistrée : ' + pos.coords.latitude.toFixed(4) + ', ' + pos.coords.longitude.toFixed(4);
    }, function() {
        status.textContent = 'Impossible d\'obtenir la position GPS.';
    });
});
</script>

<jsp:include page="../commun/footer.jsp"/>
