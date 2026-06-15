<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#fff5f8">
    <title>HoTia — <c:out value="${param.titre != null ? param.titre : 'Rencontres'}"/></title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,500;0,600;1,500&family=Syne:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/hotia.css">
</head>
<body class="hotia-body theme-${param.theme != null ? param.theme : 'app'}">
<jsp:include page="icons.jsp"/>
<div class="ambient" aria-hidden="true">
    <span class="ambient-orb ambient-orb--a"></span>
    <span class="ambient-orb ambient-orb--b"></span>
    <span class="ambient-grain"></span>
</div>

<header class="site-header">
    <div class="container header-shell">
        <a href="${pageContext.request.contextPath}${not empty sessionScope.utilisateurConnecte ? '/decouvrir' : '/connexion'}" class="brand" aria-label="HoTia accueil">
            <span class="brand-mark">
                <svg class="icon icon--lg" aria-hidden="true"><use href="#icon-mark"/></svg>
            </span>
            <span class="brand-text">
                <span class="brand-name">HoTia</span>
                <span class="brand-tag">Rencontres · Madagascar</span>
            </span>
        </a>

        <c:if test="${not empty sessionScope.utilisateurConnecte}">
            <button type="button" class="nav-toggle" id="navToggle" aria-expanded="false" aria-controls="navMain">
                <span class="nav-toggle-bar"></span>
                <span class="nav-toggle-bar"></span>
            </button>
            <nav class="nav-main" id="navMain" aria-label="Navigation principale">
                <a href="${pageContext.request.contextPath}/decouvrir" class="nav-link">
                    <svg class="icon" aria-hidden="true"><use href="#icon-discover"/></svg>
                    <span>Découvrir</span>
                </a>
                <a href="${pageContext.request.contextPath}/demandes" class="nav-link">
                    <svg class="icon" aria-hidden="true"><use href="#icon-inbox"/></svg>
                    <span>Demandes</span>
                </a>
                <c:if test="${sessionScope.utilisateurConnecte.statut == 'en_matching'}">
                    <a href="${pageContext.request.contextPath}/chat" class="nav-link">
                        <svg class="icon" aria-hidden="true"><use href="#icon-chat"/></svg>
                        <span>Messages</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/separation" class="nav-link nav-link--muted">
                        <svg class="icon" aria-hidden="true"><use href="#icon-link"/></svg>
                        <span>Séparation</span>
                    </a>
                </c:if>
                <a href="${pageContext.request.contextPath}/modifier-profil" class="nav-link">
                    <svg class="icon" aria-hidden="true"><use href="#icon-profile"/></svg>
                    <span>Profil</span>
                </a>
                <a href="${pageContext.request.contextPath}/deconnexion" class="nav-link nav-link--exit">
                    <svg class="icon" aria-hidden="true"><use href="#icon-logout"/></svg>
                    <span>Quitter</span>
                </a>
            </nav>
        </c:if>
    </div>
</header>

<main class="page-main">
    <div class="container page-container">
