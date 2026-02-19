# Comparaison Framework.jar vs Spring Web MVC

## 1. ReservationController

### ❌ AVANT (framework.jar)

```java
package com.hotel.controller;

import com.hotel.service.ReservationService;
import servlet.annotations.Controller;
import servlet.ModelView;
import servlet.annotations.mapping.GetMapping;
import servlet.annotations.RequestParam;

@Controller
public class ReservationController {

    private ReservationService reservationService = new ReservationService();

    @GetMapping(value = "/reservations")
    public ModelView getReservations(@RequestParam(name = "date") String dateStr) {
        ModelView mv = new ModelView();
        try {
            String jsonResponse;
            if (dateStr != null && !dateStr.isEmpty()) {
                jsonResponse = reservationService.getReservationsByDateJson(dateStr);
            } else {
                jsonResponse = reservationService.getAllReservationsJson();
            }
            mv.addAttribute("jsonResponse", jsonResponse);
            mv.setView("json-response.jsp");  // ← Dépend d'une JSP pour retourner JSON
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "Erreur de connexion au BackOffice";
            mv.addAttribute("jsonResponse",
                    "{\"status\":\"error\",\"code\":500,\"message\":\"" + errorMsg + "\",\"data\":null}");
            mv.setView("json-response.jsp");
        }
        return mv;
    }
}
```

### ✅ APRÈS (Spring Web MVC)

```java
package com.hotel.controller;

import com.hotel.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController                          // ← Retourne directement du JSON
@RequestMapping("/reservations")         // ← Préfixe pour tous les mappings
public class ReservationController {

    @Autowired                           // ← Injection automatique Spring
    private ReservationService reservationService;

    @GetMapping                          // ← Mapping simplifié (sans "value")
    public String getReservations(@RequestParam(name = "date", required = false) String dateStr) {
        // ← required = false au lieu de vérifier null
        try {
            if (dateStr != null && !dateStr.isEmpty()) {
                return reservationService.getReservationsByDateJson(dateStr);  // ← Retour direct
            } else {
                return reservationService.getAllReservationsJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "Erreur de connexion au BackOffice";
            return "{\"status\":\"error\",\"code\":500,\"message\":\"" + errorMsg + "\",\"data\":null}";
        }
    }
}
```

**Avantages Spring :**
- ✅ `@RestController` retourne directement JSON (pas besoin de JSP)
- ✅ `@Autowired` gère l'injection automatiquement
- ✅ Pas de `ModelView`, retour simple
- ✅ Code plus concis et lisible


## 2. ReservationService

### ❌ AVANT (framework.jar)

```java
public class ReservationService {

    private String backofficeBaseUrl;

    public ReservationService() {
        loadConfig();  // ← Chargement manuel dans le constructeur
    }

    private void loadConfig() {
        try {
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader()
                    .getResourceAsStream("api.properties");
            if (input != null) {
                props.load(input);
                backofficeBaseUrl = props.getProperty("backoffice.api.url", "http://localhost:8080/backoffice");
                input.close();
            } else {
                backofficeBaseUrl = "http://localhost:8080/backoffice";
            }
        } catch (IOException e) {
            e.printStackTrace();
            backofficeBaseUrl = "http://localhost:8080/backoffice";
        }
    }
    // ... reste du code
}
```

### ✅ APRÈS (Spring Web MVC)

```java
@Service                                 // ← Annotation Spring
public class ReservationService {

    @Value("${backoffice.api.url:http://localhost:8080/backoffice}")  // ← Injection de propriété simplifiée
    private String backofficeBaseUrl;
    
    // Plus de constructeur, plus de chargement manuel !
    
    // ... reste du code identique
}
```

**Avantages Spring :**
- ✅ `@Value` injecte les propriétés automatiquement
- ✅ Pas de chargement manuel de fichier
- ✅ Valeur par défaut intégrée
- ✅ Plus facile à tester et configurer


## 3. PageController

### ❌ AVANT (framework.jar)

```java
@Controller
public class PageController {

    @GetMapping(value = "/pages/")
    public ModelView index() {
        ModelView mv = new ModelView();
        mv.setView("pages/index.jsp");
        return mv;
    }

    @GetMapping(value = "/pages/liste-reservations")
    public ModelView listeReservations() {
        ModelView mv = new ModelView();
        mv.setView("pages/liste-reservations.jsp");
        return mv;
    }
}
```

### ✅ APRÈS (Spring Web MVC)

```java
@Controller
public class PageController {

    @GetMapping(value = "/pages/")
    public String index() {
        return "pages/index.jsp";  // ← Retour direct du chemin
    }

    @GetMapping(value = "/pages/liste-reservations")
    public String listeReservations() {
        return "pages/liste-reservations.jsp";
    }
}
```

**Avantages Spring :**
- ✅ Retour simple du chemin (String)
- ✅ Pas d'objet `ModelView`
- ✅ ViewResolver gère automatiquement la résolution


## 4. Configuration Web

### ❌ AVANT (web.xml framework.jar)

```xml
<web-app>
    <servlet>
        <servlet-name>FrontServlet</servlet-name>
        <servlet-class>servlet.FrontServlet</servlet-class>
        <init-param>
            <param-name>controller-package</param-name>
            <param-value>com.hotel.controller</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>FrontServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

### ✅ APRÈS (web.xml Spring Web MVC)

```xml
<web-app>
    <!-- Context Loader -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/applicationContext.xml</param-value>
    </context-param>
    
    <!-- Spring DispatcherServlet -->
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/spring-servlet.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

**Avantages Spring :**
- ✅ DispatcherServlet standard, très bien documenté
- ✅ Séparation claire contexte global / contexte servlet
- ✅ Configuration externalisée dans des fichiers XML
- ✅ Meilleure interopérabilité avec autres outils Spring


## 5. Dépendances (pom.xml)

### ❌ AVANT
```xml
<dependency>
    <groupId>framework</groupId>
    <artifactId>framework</artifactId>
    <version>1.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/framework.jar</systemPath>
</dependency>

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

### ✅ APRÈS
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>6.1.4</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>6.1.4</version>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.20.1</version>
</dependency>
```

**Avantages Spring :**
- ✅ Dépendances Maven opérationnelles (pas de JAR local)
- ✅ Jackson est plus performant que Gson
- ✅ Accès à un écosystème énorme de librairies Spring


## Résumé des avantages

| Aspect | framework.jar | Spring Web MVC |
|--------|---------------|---|
| **Injection de dépendances** | Manuel | Automatique (@Autowired) |
| **Configuration** | Annotations personnalisées | Annotations standard |
| **Documentation** | Limitée | Excellente (communauté large) |
| **Performance** | Acceptable | Optimisée |
| **Testabilité** | Difficile | Facile |
| **Intégration multi-outils** | Complexe | Native |
| **Courbe d'apprentissage** | Moyenne | Faible (très populaire) |
| **Support communautaire** | Aucun | Excellent |
