# Parcours d’apprentissage Full-Stack — ODC Cohorte 3

Ce dépôt retrace mon parcours d’apprentissage en développement logiciel dans le cadre de ma formation à ODC. J’y consigne les étapes de mon évolution et j’approfondis mes connaissances par la pratique, avec l’aide de mon assistant IA.

## Projet actuel : API de gestion des employés

Le projet contient une API REST développée avec Java et Spring Boot. Elle permet de gérer des employés et met en pratique les bases d’une architecture backend en couches : contrôleurs, services, dépôts, modèles, DTO et mappers.

### Fonctionnalités présentes

- Création, consultation, mise à jour complète ou partielle et suppression d’employés.
- Authentification par identifiant et mot de passe, avec émission de jetons JWT.
- Protection des routes avec Spring Security et encodage des mots de passe avec BCrypt.
- Validation des données reçues par l’API.
- Documentation OpenAPI/Swagger.
- Tests unitaires et tests d’intégration.

> Le dépôt contient actuellement une API backend ; aucune application frontend n’est présente pour le moment.

## Technologies

- Java 21
- Spring Boot 3.2.3
- Spring Web, Spring Data JPA et Spring Security
- JWT (JJWT)
- H2 pour les tests et PostgreSQL comme dépendance disponible
- Maven et Maven Wrapper
- OpenAPI/Swagger

## Structure du dépôt

```text
.
├── API/
│   ├── pom.xml
│   ├── mvnw                 # Wrapper Maven pour macOS/Linux
│   ├── mvnw.cmd             # Wrapper Maven pour Windows
│   ├── postman/             # Ressources de requêtes Postman
│   └── src/
│       ├── main/java/com/example/api/
│       │   ├── config/      # Configuration sécurité et OpenAPI
│       │   ├── controller/ # Contrôleurs REST (authentification, employés)
│       │   ├── dto/         # Objets d’entrée et de réponse
│       │   ├── mapper/      # Conversion entre DTO et entités
│       │   ├── model/       # Entités métier
│       │   ├── repository/  # Accès aux données
│       │   ├── security/    # JWT et gestion de l’identité
│       │   └── service/     # Logique métier
│       ├── main/resources/  # Configuration et données d’initialisation
│       └── test/            # Tests et configuration de test
└── README.md
```

## Prérequis

- JDK 21
- Git

Le Maven Wrapper est fourni dans le dossier `API` : une installation séparée de Maven n’est donc pas nécessaire.

## Lancer l’API

Depuis la racine du dépôt :

```bash
cd API
./mvnw spring-boot:run
```

Sous Windows, utiliser `mvnw.cmd` à la place de `./mvnw`.

L’application est configurée pour démarrer sur le port **9001**. La console H2 est configurée à l’adresse `http://localhost:9001/h2-console`.

## Lancer les tests

Depuis le dossier `API` :

```bash
./mvnw test
```

La configuration de test utilise une base H2 en mémoire.

## Principales routes REST

| Méthode | Route | Description |
| --- | --- | --- |
| `POST` | `/login` | Authentifier un utilisateur et obtenir un JWT |
| `POST` | `/employee` | Créer un employé |
| `GET` | `/employees` | Lister les employés |
| `GET` | `/employee/{id}` | Consulter un employé |
| `PUT` | `/employee/{id}` | Remplacer les données d’un employé |
| `PATCH` | `/employee/{id}` | Modifier partiellement un employé |
| `DELETE` | `/employee/{id}` | Supprimer un employé |
| `DELETE` | `/employees` | Supprimer tous les employés |

Les routes qui nécessitent une authentification doivent recevoir le jeton dans l’en-tête HTTP :

```http
Authorization: Bearer <votre-token>
```

Les routes publiques et protégées sont définies dans `API/src/main/java/com/example/api/config/SecurityConfig.java`.

## Documentation de l’API

La dépendance Springdoc OpenAPI est incluse. Lorsque l’application est lancée, l’interface Swagger UI est disponible à l’adresse habituelle :

```text
http://localhost:9001/swagger-ui/index.html
```

## Configuration de la base de données

La configuration PostgreSQL est présente en commentaire dans `API/src/main/resources/application.properties`. Pour utiliser PostgreSQL, configurez l’URL, l’utilisateur et le mot de passe de la base dans l’environnement local, sans committer de secrets dans le dépôt. La base H2 reste adaptée aux tests.

## Journal d’apprentissage

Ce dépôt évoluera au fil de la formation. Chaque étape pourra documenter les notions abordées, les exercices réalisés, les difficultés rencontrées et les améliorations apportées au projet.

---

Projet réalisé dans le cadre de ma formation à ODC — Cohorte 3.
