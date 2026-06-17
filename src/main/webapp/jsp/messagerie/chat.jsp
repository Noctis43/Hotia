<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../commun/header.jsp">
    <jsp:param name="titre" value="Messages"/>
    <jsp:param name="theme" value="app"/>
</jsp:include>

<header class="page-head">
    <h1>Messages</h1>
    <p class="page-lead">Conversation avec <c:out value="${partenaire.prenom}"/></p>
</header>

<c:if test="${not empty sessionScope.flashErreur}">
    <div class="alert alert-error" role="alert"><c:out value="${sessionScope.flashErreur}"/></div>
    <c:remove var="flashErreur" scope="session"/>
</c:if>

<section class="card chat-section">
    <div class="chat-messages" id="chatMessages" role="log" aria-live="polite">
        <c:forEach var="msg" items="${messages}">
            <div class="message ${msg.emetteurId == utilisateurConnecte.id ? 'message-moi' : 'message-autre'}">
                <p class="message-contenu"><c:out value="${msg.contenu}"/></p>
                <time class="message-date"><c:out value="${msg.dateEnvoi}"/></time>
            </div>
        </c:forEach>
        <c:if test="${empty messages}">
            <p class="hint">Aucun message. Écrivez le premier.</p>
        </c:if>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/chat" class="chat-form">
        <label class="visually-hidden" for="contenu">Votre message</label>
        <textarea id="contenu" name="contenu" rows="2" placeholder="Écrire un message..." required></textarea>
        <button type="submit" class="btn btn-primary" aria-label="Envoyer le message">
            <svg class="icon" aria-hidden="true"><use href="#icon-send"/></svg>
            <span>Envoyer</span>
        </button>
    </form>
</section>

<script>
(function () {
    var box = document.getElementById('chatMessages');
    if (box) { box.scrollTop = box.scrollHeight; }
})();
</script>

<jsp:include page="../commun/footer.jsp"/>
