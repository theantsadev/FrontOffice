# Guide de test du FrontOffice Spring Web MVC

## 1. Prérequis
- Tomcat 9+ déployé et en cours d'exécution
- BackOffice déployé sur http://localhost:8080/backoffice
- MySQL/PostgreSQL fonctionnelle avec les données de test

## 2. Build du FrontOffice

```bash
cd FrontOffice
mvn clean package
```

Le WAR généré sera dans `target/frontoffice.war`

## 3. Déploiement

Copier `frontoffice.war` dans `TOMCAT_HOME/webapps/`

Tomcat devrait déployer automatiquement l'application sur http://localhost:8080/frontoffice

## 4. Points d'accès principales

### Page d'accueil
```
http://localhost:8080/frontoffice/pages/
```

### Liste des réservations
```
http://localhost:8080/frontoffice/pages/liste-reservations
```

### API REST - Toutes les réservations
```
GET http://localhost:8080/frontoffice/reservations
```

Réponse (JSON provenant du BackOffice):
```json
{
  "status": "success",
  "code": 200,
  "message": "Reservations retrieved successfully",
  "data": [
    {
      "id_reservation": 1,
      "id_client": "CLI001",
      "nom_hotel": "Hotel Luxe",
      "nb_passager": 2,
      "date_heure_arrivee": "2026-02-20T14:30:00"
    }
  ]
}
```

### API REST - Réservations par date
```
GET http://localhost:8080/frontoffice/reservations?date=2026-02-20
```

## 5. Tests fonctionnels

### Test 1: Accès à la page d'accueil
```bash
curl http://localhost:8080/frontoffice/pages/
```
✅ Devrait retourner le HTML d'accueil

### Test 2: Accès à la liste des réservations
```bash
curl http://localhost:8080/frontoffice/pages/liste-reservations
```
✅ Devrait retourner la page avec le tableau de réservations

### Test 3: Récupérer toutes les réservations (API)
```bash
curl -H "Accept: application/json" http://localhost:8080/frontoffice/reservations
```
✅ Devrait retourner du JSON valide

### Test 4: Filtrer les réservations par date
```bash
curl -H "Accept: application/json" "http://localhost:8080/frontoffice/reservations?date=2026-02-20"
```
✅ Devrait retourner les réservations de cette date

## 6. Vérification de la console

### Logs Spring à attendre
```
DispatcherServlet: Frame buffer capacity: ...
DispatcherServlet: Initializing servlet 'dispatcher'
DispatcherServlet: Completed initialization in X ms
Mapped "" from class path resource [/WEB-INF/spring-servlet.xml]
```

### Erreurs à éviter
```
ClassNotFoundException: org.springframework.web.servlet.DispatcherServlet
→ Vérifier que les dépendances Spring sont dans WEB-INF/lib/

org.springframework.context.applicationcontext: Element 'context:component-scan'
→ Vérifier le namespace Spring dans les fichiers XML

No mapping for GET /reservations
→ Vérifier que le @RestController et @GetMapping sont corrects
```

## 7. Débogage

### Activer les logs DEBUG
Modifier `src/main/resources/log4j.properties` (ou créer si absent):
```properties
log4j.rootLogger=DEBUG, CONSOLE
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.layout.ConversionPattern=%d [%t] %-5p %c - %m%n
```

### Vérifier les composants Spring
```java
// Ajouter dans un controller temporaire:
@Autowired
private ApplicationContext context;

@GetMapping("/debug/beans")
public List<String> listBeans() {
    return Arrays.asList(context.getBeanDefinitionNames());
}
```

## 8. Migration depuis l'ancien système

### Anciens fichiers qui peuvent être supprimés
- lib/framework.jar (plus utilisé)
- src/main/webapp/json-response.jsp (optionnel)

### Fichiers à conserver
- src/main/webapp/pages/*.jsp
- src/main/java/com/hotel/model/*.java
- src/main/resources/api.properties

## Notes importantes

1. **Spring Boot vs Spring MVC** : Ceci utilise Spring MVC classique avec Tomcat (pas Spring Boot)
2. **Compatibility** : Fonctionne avec n'importe quel serveur Servlet compatible (Tomcat, JBoss, etc.)
3. **Performance** : Spring Web MVC offre généralement de meilleures performances que le framework.jar
4. **Maintenance** : Beaucoup plus facile à maintenir et à déboguer grâce aux outils Spring populaires
