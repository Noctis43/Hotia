<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="u" value="${profilData.utilisateur}"/>
<c:set var="p" value="${profilData.profil}"/>
<c:set var="prefR" value="${profilData.preferenceRecherche}"/>
<c:set var="prefD" value="${profilData.preferenceDescription}"/>
<jsp:include page="../commun/header.jsp">
    <jsp:param name="titre" value="Modifier le profil"/>
    <jsp:param name="theme" value="form"/>
</jsp:include>

<header class="page-head">
    <h1>Modifier mon profil</h1>
    <p class="page-lead">Affinez votre présence et vos préférences de rencontre.</p>
</header>

<section class="card form-large">
    <c:if test="${not empty erreurs}">
        <div class="alert alert-error" role="alert"><ul>
            <c:forEach var="msg" items="${erreurs}">
                <li><c:out value="${msg}"/></li>
            </c:forEach>
        </ul></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/modifier-profil"
          enctype="multipart/form-data" class="form-sections">

        <fieldset>
            <legend>Photo de profil</legend>
            <c:if test="${not empty profilData.photos}">
                <img src="${pageContext.request.contextPath}/${profilData.photos[0].chemin}" alt="Photo actuelle" class="preview-photo">
            </c:if>
            <div class="file-drop">
                <input type="file" name="photo" accept="image/*" aria-label="Nouvelle photo de profil">
                <p class="hint">Laissez vide pour conserver la photo actuelle.</p>
            </div>
        </fieldset>

        <fieldset>
            <legend>Téléphone</legend>
            <input type="tel" name="telephone" value="<c:out value='${p.telephone}'/>"
                   pattern="03[0-9] [0-9]{2} [0-9]{3} [0-9]{2}" required autocomplete="tel">
        </fieldset>

        <fieldset>
            <legend>Biographie</legend>
            <textarea name="bio" maxlength="500" rows="4"><c:out value="${p.bio}"/></textarea>
        </fieldset>

        <fieldset>
            <legend>Localisation</legend>
            <label>Ville
                <select name="villeId" id="villeId">
                    <option value="">-- GPS ou ville --</option>
                    <c:forEach var="ville" items="${villes}">
                        <option value="${ville.id}" ${p.villeId == ville.id ? 'selected' : ''}>
                            <c:out value="${ville.nom}"/>
                        </option>
                    </c:forEach>
                </select>
            </label>
            <button type="button" id="btnGps" class="btn btn-secondary">
                <svg class="icon" aria-hidden="true"><use href="#icon-gps"/></svg>
                Activer le GPS
            </button>
            <input type="hidden" name="latitude" id="latitude" value="<c:out value='${p.latitude}'/>">
            <input type="hidden" name="longitude" id="longitude" value="<c:out value='${p.longitude}'/>">
            <p id="gpsStatus" class="hint"></p>
        </fieldset>

        <fieldset>
            <legend>Préférences de recherche</legend>
            <div class="checkbox-grid">
                <c:forEach var="entry" items="${divertissementsRef}">
                    <label>
                        <input type="checkbox" name="divertissementsRecherche" value="${entry.key}"
                            <c:forEach var="sel" items="${prefR.divertissements}">
                                <c:if test="${sel == entry.value}">checked</c:if>
                            </c:forEach>>
                        <c:out value="${entry.value}"/>
                    </label>
                </c:forEach>
            </div>
            <div class="checkbox-grid">
                <c:forEach var="entry" items="${comportementsRef}">
                    <label>
                        <input type="checkbox" name="comportementsRecherche" value="${entry.key}"
                            <c:forEach var="sel" items="${prefR.comportements}">
                                <c:if test="${sel == entry.value}">checked</c:if>
                            </c:forEach>>
                        <c:out value="${entry.value}"/>
                    </label>
                </c:forEach>
            </div>
            <label>Sexe recherché
                <select name="sexeCibleRecherche">
                    <option value="Peu importe" ${prefR.sexeCible == 'Peu importe' ? 'selected' : ''}>Peu importe</option>
                    <option value="Homme" ${prefR.sexeCible == 'Homme' ? 'selected' : ''}>Homme</option>
                    <option value="Femme" ${prefR.sexeCible == 'Femme' ? 'selected' : ''}>Femme</option>
                    <option value="Autre" ${prefR.sexeCible == 'Autre' ? 'selected' : ''}>Autre</option>
                </select>
            </label>
            <label>Situation familiale
                <select name="situationRecherche">
                    <option value="Peu importe" ${prefR.situationFamiliale == 'Peu importe' ? 'selected' : ''}>Peu importe</option>
                    <option value="Célibataire" ${prefR.situationFamiliale == 'Célibataire' ? 'selected' : ''}>Célibataire</option>
                    <option value="Divorcé" ${prefR.situationFamiliale == 'Divorcé' ? 'selected' : ''}>Divorcé</option>
                    <option value="Veuf" ${prefR.situationFamiliale == 'Veuf' ? 'selected' : ''}>Veuf</option>
                </select>
            </label>
            <label>Type de relation
                <select name="typeRelationRecherche">
                    <option value="Peu importe" ${prefR.typeRelation == 'Peu importe' ? 'selected' : ''}>Peu importe</option>
                    <option value="Relation sérieuse" ${prefR.typeRelation == 'Relation sérieuse' ? 'selected' : ''}>Relation sérieuse</option>
                    <option value="Amitié" ${prefR.typeRelation == 'Amitié' ? 'selected' : ''}>Amitié</option>
                    <option value="Aventure" ${prefR.typeRelation == 'Aventure' ? 'selected' : ''}>Aventure</option>
                </select>
            </label>
            <div class="row-2">
                <label>Âge min <input type="number" name="ageMinRecherche" value="${prefR.ageMin}" min="18" max="99"></label>
                <label>Âge max <input type="number" name="ageMaxRecherche" value="${prefR.ageMax}" min="18" max="99"></label>
            </div>
        </fieldset>

        <fieldset>
            <legend>Description personnelle</legend>
            <div class="checkbox-grid">
                <c:forEach var="entry" items="${divertissementsRef}">
                    <label>
                        <input type="checkbox" name="divertissementsDescription" value="${entry.key}"
                            <c:forEach var="sel" items="${prefD.divertissements}">
                                <c:if test="${sel == entry.value}">checked</c:if>
                            </c:forEach>>
                        <c:out value="${entry.value}"/>
                    </label>
                </c:forEach>
            </div>
            <div class="checkbox-grid">
                <c:forEach var="entry" items="${comportementsRef}">
                    <label>
                        <input type="checkbox" name="comportementsDescription" value="${entry.key}"
                            <c:forEach var="sel" items="${prefD.comportements}">
                                <c:if test="${sel == entry.value}">checked</c:if>
                            </c:forEach>>
                        <c:out value="${entry.value}"/>
                    </label>
                </c:forEach>
            </div>
            <label>Sexe
                <select name="sexeCibleDescription">
                    <option value="Homme" ${prefD.sexeCible == 'Homme' ? 'selected' : ''}>Homme</option>
                    <option value="Femme" ${prefD.sexeCible == 'Femme' ? 'selected' : ''}>Femme</option>
                    <option value="Autre" ${prefD.sexeCible == 'Autre' ? 'selected' : ''}>Autre</option>
                </select>
            </label>
            <label>Situation familiale
                <select name="situationDescription">
                    <option value="Célibataire" ${prefD.situationFamiliale == 'Célibataire' ? 'selected' : ''}>Célibataire</option>
                    <option value="Divorcé" ${prefD.situationFamiliale == 'Divorcé' ? 'selected' : ''}>Divorcé</option>
                    <option value="Veuf" ${prefD.situationFamiliale == 'Veuf' ? 'selected' : ''}>Veuf</option>
                </select>
            </label>
            <label>Type de relation
                <select name="typeRelationDescription">
                    <option value="Relation sérieuse" ${prefD.typeRelation == 'Relation sérieuse' ? 'selected' : ''}>Relation sérieuse</option>
                    <option value="Amitié" ${prefD.typeRelation == 'Amitié' ? 'selected' : ''}>Amitié</option>
                    <option value="Aventure" ${prefD.typeRelation == 'Aventure' ? 'selected' : ''}>Aventure</option>
                </select>
            </label>
            <div class="row-2">
                <label>Âge min <input type="number" name="ageMinDescription" value="${prefD.ageMin}" min="18" max="99"></label>
                <label>Âge max <input type="number" name="ageMaxDescription" value="${prefD.ageMax}" min="18" max="99"></label>
            </div>
        </fieldset>

        <button type="submit" class="btn btn-primary btn-block">Enregistrer les modifications</button>
    </form>
</section>

<script>
document.getElementById('btnGps').addEventListener('click', function() {
    navigator.geolocation.getCurrentPosition(function(pos) {
        document.getElementById('latitude').value = pos.coords.latitude;
        document.getElementById('longitude').value = pos.coords.longitude;
        document.getElementById('villeId').value = '';
        document.getElementById('gpsStatus').textContent = 'GPS : ' + pos.coords.latitude.toFixed(4) + ', ' + pos.coords.longitude.toFixed(4);
    });
});
</script>

<jsp:include page="../commun/footer.jsp"/>
