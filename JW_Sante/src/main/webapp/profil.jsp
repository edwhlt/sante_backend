<%--
  Created by IntelliJ IDEA.
  User: xehx
  Date: 23/06/2021
  Time: 22:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Créer son profil</title>
</head>
<body>

<form action="api/profil"  method="post">
    <label for="name">Entrez votre nom</label><input name="name" id="name" placeholder="Nom">
    <br>
    <label for="weight">Entrez votre poids</label><input name="weight" id="weight" placeholder="Poids">
    <br>
    <label for="size">Entrez votre taille</label><input name="size" id="size" placeholder="Taile">
    <br>
    <label for="age">Entrez votre age</label><input name="age" id="age" placeholder="Age">
    <br>

    <label for="genre">Genre</label>
    <select name="genre" id="genre">
        <option value="0">Homme</option>
        <option value="1">Femme</option>
    </select>
    <br>
    <label for="activity">Activité physique</label>
    <select name="activity" id="activity">
        <option value="1.2">1.2</option>
        <option value="1.375">1.375</option>
    </select>
    <br>
    <label for="factor">Facteur</label>
    <select name="factor" id="factor">
        <option value="0.9">0.9</option>
        <option value="1.1">1.1</option>
        <option value="1.15">1.15</option>
    </select>
    <br>
    <input type="submit" value="Ajouter le profil" />
</form>

</body>
</html>
