# FamilyTreeBack

API REST de l'application de création d'arbres généalogiques (Java 21, Spring Boot 3, MySQL).

## Lancer avec Docker

```bash
cp .env.example .env
docker compose up --build
```

| Service    | URL                       |
|------------|---------------------------|
| API        | http://localhost:8080/api |
| phpMyAdmin | http://localhost:8081     |
| MySQL      | localhost:3307            |

Le front se trouve dans le repo FamilyTreeFront.

## Développement local

```bash
docker compose up db phpmyadmin
mvn spring-boot:run
```

## API

- `GET/POST /api/trees`, `PUT/DELETE /api/trees/{id}`
- `GET/POST /api/trees/{treeId}/persons`
- `PUT/DELETE /api/persons/{id}`
