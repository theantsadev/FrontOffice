# Migration FrontOffice vers Spring Web MVC

## Changements effectués

### 1. **pom.xml**
- Supprimé la dépendance au `framework.jar` personnalisé
- Ajouté les dépendances Spring Web MVC (6.1.4)
- Remplacé Gson par Jackson pour le JSON

### 2. **web.xml**
- Remplacé le `FrontServlet` personnalisé par Spring `DispatcherServlet`
- Ajouté `ContextLoaderListener` pour l'initialisation du contexte Spring
- Configuration des chemins vers les fichiers Spring

### 3. **Nouveaux fichiers de configuration Spring**
- **applicationContext.xml** : Configuration globale de l'application
  - Component scan pour les services
  - Chargement des propriétés
  
- **spring-servlet.xml** : Configuration du DispatcherServlet
  - Component scan pour les controllers
  - ViewResolver pour JSP
  - Annotations MVC activées

### 4. **PageController.java**
- Annotations Spring : `@Controller`, `@GetMapping` (Spring MVC)
- Retourné directement le chemin JSP au lieu de `ModelView`

### 5. **ReservationController.java**
- Changé en `@RestController` pour retourner du JSON directement
- Utilisation de `@RequestParam` Spring au lieu du framework personnalisé
- Injection automatique avec `@Autowired`
- Endpoint : `GET /reservations` (avec ou sans paramètre `date`)

### 6. **ReservationService.java**
- Annotation `@Service` pour l'injection Spring
- Injection de propriétés avec `@Value` au lieu de chargement manuel
- Simplification du code grâce à Spring

## Structure des endpoints

### Avant (framework.jar)
```
GET /pages/ → pages/index.jsp
GET /pages/liste-reservations → pages/liste-reservations.jsp
GET /reservations → json-response.jsp (retourne JSON)
```

### Après (Spring Web MVC)
```
GET /pages/ → pages/index.jsp
GET /pages/liste-reservations → pages/liste-reservations.jsp
GET /reservations → JSON directement (RestController)
```

## Configuration requise

### Propriétés (api.properties)
```properties
backoffice.api.url=http://localhost:8080/backoffice
```

## Build et exécution

```bash
# Build
mvn clean package

# Déploiement sur Tomcat
# Copier le WAR généré (target/frontoffice.war) dans webapps/
```

## Points importants

1. **Les JSP ne changent pas** : Le JavaScript côté client reste identique
2. **L'API REST retourne du JSON direct** : Plus besoin de json-response.jsp
3. **Injection de dépendances** : Spring gère automatiquement les services
4. **Configuration externalisée** : Les propriétés sont chargées depuis api.properties
5. **Compatible avec le BackOffice** : L'API reste identique

## Notes de compatibilité

- Le fichier `json-response.jsp` n'est plus utilisé mais peut être conservé
- Le dossier `lib/` contenant `framework.jar` n'est plus nécessaire
- Les modèles (Hotel.java, Reservation.java) ne sont pas utilisés mais conservés
