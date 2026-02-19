# Guide de Test - FrontOffice

## Déploiement

### Build et Déploiement

```bash
.\build.bat
```

Le script `build.bat` :
1. Nettoie les fichiers de build existants
2. Compile le projet avec Maven (`mvn package`)
3. Génère `frontoffice.war`
4. Déploie vers Tomcat : `%TOMCAT_HOME%\webapps\frontoffice.war`
5. (Optionnel) Démarre Tomcat automatiquement

**Résultat :** Application disponible à **`http://localhost:8080/frontoffice/pages/`**

---

## Pages et Fonctionnalités

### 1. Accueil
- **URL** : `http://localhost:8080/frontoffice/pages/`
- **Fichier** : `src/main/webapp/pages/index.jsp`
- **Contenu** : Menu avec lien vers "Voir les Réservations"

### 2. Liste des Réservations
- **URL** : `http://localhost:8080/frontoffice/pages/liste-reservations`
- **Fichier** : `src/main/webapp/pages/liste-reservations.jsp`
- **Fonctionnalités** :
  - Affichage du tableau de toutes les réservations
  - Recherche par date (format : yyyy-MM-dd)
  - Bouton "Afficher tout" pour réinitialiser le filtre
  - Chargement asynchrone avec spinner

---

## Architecture et API

### Endpoints FrontOffice

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/reservations` | Récupère toutes les réservations (via BackOffice) |
| GET | `/reservations?date=yyyy-MM-dd` | Filtre les réservations par date (via BackOffice) |

### Flux de Données

```
Navigateur
  ↓ (JavaScript fetch)
FrontOffice ReservationController (/reservations)
  ↓ (HTTP GET)
ReservationService
  ↓ (HTTP GET)
BackOffice API (http://localhost:8080/backoffice/reservations)
  ↓ (JSON response)
FrontOffice
  ↓ (JSON response)
Navigateur (affichage tableau)
```

### Configuration API

**Fichier** : `src/main/resources/api.properties`
```properties
backoffice.api.url=http://localhost:8080/backoffice
```

---

## Tests Manuels

### Test 1 : Affichage de toutes les réservations
1. Accéder à `http://localhost:8080/frontoffice/pages/liste-reservations`
2. Attendre le chargement (spinner)
3. Vérifier que le tableau contient les colonnes : ID Réservation, ID Client, Hôtel, Nombre de passagers, Date et heure d'arrivée

### Test 2 : Recherche par date
1. Sélectionner une date dans le sélecteur de date
2. Cliquer sur "Rechercher"
3. Vérifier que le tableau affiche uniquement les réservations de cette date

### Test 3 : Afficher tout
1. Appliquer un filtre par date
2. Cliquer sur "Afficher tout"
3. Vérifier que le tableau affiche à nouveau toutes les réservations

### Test 4 : Gestion des erreurs
1. Si le BackOffice n'est pas accessible, un message d'erreur doit s'afficher
2. Vérifier que le message indique l'erreur de connexion

---

## Dépendances

- **Java** : 21+
- **Tomcat** : 10.1.24+
- **Framework** : Custom servlet framework (lib/framework.jar)
- **Bibliothèques** : Gson, Bootstrap 5, JSTL

---

## Fichiers Clés

| Fichier | Rôle |
|---------|------|
| `src/main/java/com/hotel/controller/ReservationController.java` | Proxy vers l'API BackOffice |
| `src/main/java/com/hotel/controller/PageController.java` | Serveur des pages JSP |
| `src/main/java/com/hotel/service/ReservationService.java` | Client HTTP pour le BackOffice |
| `src/main/webapp/pages/liste-reservations.jsp` | Interface de visualisation |
| `src/main/resources/api.properties` | Configuration de l'URL BackOffice |
| `pom.xml` | Dépendances Maven |
| `build.bat` | Script de build et déploiement |

