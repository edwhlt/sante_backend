<%@ page import="java.util.*" %>
<%@ page import="fr.hedwin.objects.*" %>
<%@ page import="static fr.hedwin.objects.Table.round" %>
<%@ page import="fr.hedwin.objects.elements.Repas" %>
<%@ page import="fr.hedwin.objects.elements.Recette" %>
<%@ page import="fr.hedwin.objects.Profil" %>
<%@ page import="fr.hedwin.objects.elements.Week" %>
<%@ page import="fr.hedwin.objects.aliment.Aliment" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<%--
  Created by IntelliJ IDEA.
  User: xehx
  Date: 08/02/2021
  Time: 14:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <script src="js/index.js"></script>
</head>
<body>
<%
    Profil profil = (Profil) request.getAttribute("profil");
    Map<String, Recette> recettesMap = (Map<String, Recette>) request.getAttribute("recettes");
    Map<String, Repas> repasMap = (Map<String, Repas>) request.getAttribute("repas");
    Map<String, Aliment> alimentMap = (Map<String, Aliment>) request.getAttribute("aliments");
%>
<div>
    <h1>Profil</h1>
    <p>Nom : <%= profil.getName() %></p>
    <p>Poids : <%= profil.getWeight() %> kg</p>
    <p>Taille : <%= profil.getSize() %> cm</p>
    <p>Age : <%= profil.getAge() %> ans</p>
    <p>Activité physique : <%= profil.getPhysical_activity() %></p>
    <p>Facteur prise de masse : <%= profil.getFactor() %></p>
    <p>Energy Quotidienne : <%= round(profil.getEnergyPerDay()) %> kcal</p>
    <p>Quantité de glucides : <%= round(profil.getCarbohydratePerDay()) %> g</p>
    <p>Quantité de proteine : <%= round(profil.getProteinPerDay()) %> g</p>
    <p>Quantité de lipide : <%= round(profil.getLipidPerDay()) %> g</p>
</div>

<a href="api/resource/excel/<%= profil.getId() %>">Télécharger Excel</a>
<a href="api/resource/weeklyList/<%= profil.getId() %>">Télécharger list de course</a>

<h1>Recettes</h1>
<%-- <%= Table.getTable(recettesMap) %>
<h1>Repas</h1>
<%= Table.getTable(repasMap) %>
 <h1>Résultats journaliers</h1>
<%= Table.getTable(dayResults) %> --%>
</body>
</html>
