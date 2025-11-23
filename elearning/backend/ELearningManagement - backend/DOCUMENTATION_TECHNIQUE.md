# Documentation technique – Plateforme eLearning (PFE Roua)

Cette documentation décrit les choix d’architecture, les principaux modules métier et les points d’intégration de l’application **ELearningManagement** (Spring Boot 2.5 / Java 11) qui alimente le projet PFE de Roua. Le frontend Angular (non inclus dans ce dépôt) consomme les API HTTP exposées par ce backend.

## 1. Objectifs et périmètre

- **Cas d’usage couverts :** inscription utilisateurs/professeurs, gestion des cours/chapitres, inscriptions (enrollments), wishlist, quiz, paiement, commentaires/notation, messagerie interne, administration (statistiques, activation professeurs), réinitialisation du mot de passe.
- **Parties prenantes :** 
  - *Visiteur* : crée un compte (user ou professeur).
  - *Utilisateur apprenant* : suit des cours publics/premium, passe des quiz, paye pour des contenus, communique avec un professeur.
  - *Professeur* : publie des cours, valide ses statuts, répond aux messages, suit ses étudiants.
  - *Admin* : a accès aux agrégats (totaux) et au pilotage de la plateforme.

## 2. Stack technologique

| Couche | Technologie | Fichiers/icônes clés |
| --- | --- | --- |
| Backend REST | Spring Boot 2.5, Spring MVC, Spring Data JPA, Spring Security, JWT (jjwt) | `ELearningManagementApplication.java`, contrôleurs `com.application.controller.*` |
| Persistance | MySQL 5.7+/8, Hibernate, flywheel improvisé via `spring.jpa.hibernate.ddl-auto=update` | `application.properties`, repositories `com.application.repository.*` |
| Authentification | Spring Security, `JwtAuthorizationFilter`, `JwtUtil`, `CustomUserDetailsService`, `BCryptPasswordEncoder` | `SecurityConfig.java` |
| Messaging/Email | JavaMailSender (SMTP), services de messagerie | `EmailService.java` |
| Outils | Maven Wrapper (`mvnw`), Lombok 1.18 pour réduire le boilerplate, Java 11 |

## 3. Architecture logique

- **Modèle MVC Spring Boot.** Chaque ressource métier possède :
  - un *Controller* (REST, JSON),
  - un *Service* contenant la logique applicative,
  - un *Repository* (Spring Data) pour la persistance.
- **Architecture hexagonale simplifiée.** Les services ne dépendent que des repositories/interfaces. Cela facilite le test unitaire en simulant les couches externes.
- **Sécurité par middleware.** `SecurityConfig` enregistre une `SecurityFilterChain` qui désactive la CSRF (pour consommation via SPA), ouvre certains endpoints (`/login/**`, `/register**`, `/listcourses`, etc.) et applique le filtre `JwtAuthorizationFilter` avant `UsernamePasswordAuthenticationFilter`.
- **Stateless + JWT.** Chaque appel authentifié doit porter un header `Authorization: Bearer <token>` généré par `JwtUtil` lors du login (`/login/user`). Le `SecurityContext` est alimenté à chaque requête grâce au filtre.
- **Gestion des fichiers.** Les uploads d’avatars utilisent `MultipartFile`. `WebConfig` expose `/uploads/**` vers un dossier configuré par `app.upload.dir`.
- **Communication frontend.** Toutes les routes critiques sont annotées `@CrossOrigin(origins = "http://localhost:4200")` pour l’UI Angular.

## 4. Organisation du code

```
com.application
├── configuration  (Sécurité, JWT, CORS, WebConfig)
├── controller     (API REST : User, Professor, Course, Quiz, Comment, Chat, Payment, Login/Registration)
├── model          (Entités JPA + DTO + responses)
├── repository     (Interfaces CRUD Spring Data)
├── services       (Logique métier transversale)
└── ELearningManagementApplication.java (point d’entrée Spring Boot)
```

Les scripts SQL (`elearning_db_backup.sql`, etc.) sont stockés à la racine du dossier `elearning`.

## 5. Modules fonctionnels & responsabilités

| Module | Description | Artefacts principaux |
| --- | --- | --- |
| **Gestion des comptes** | CRUD user/professor, avatars, statistiques (`/userlist`, `/professorlist`, `/users/{email}`, etc.) | `UserController`, `ProfessorController`, `UserService`, `ProfessorService`, `UserRepository`, `ProfessorRepository` |
| **Authentification & mot de passe** | Login mixte user/professor (`/login/user`), JWT, réinitialisation par email (`/auth/forgot-password`) | `LoginController`, `RegistrationController`, `JwtUtil`, `EmailService`, `PasswordResetResponse` |
| **Cours & chapitres** | Création, mise à jour, listing, synchronisation du compteur d’inscrits, chapitres par cours | `CourseController`, `CourseService`, `ChapterService`, `CourseRepository`, `ChapterRepository` |
| **Inscriptions** | Enrôle un user/professor dans un cours, agrégats, export | `UserController.enrollNewCourse`, `EnrollmentService`, `EnrollmentRepository` |
| **Wishlist** | Ajout/retrait de cours favoris, calcul du statut (`/addtowishlist`, `/getwishliststatus`) | `UserController`, `WishlistService` |
| **Commentaires & notation** | Thread de commentaires multi-niveaux avec rating moyen par cours | `CommentController`, `Comment`, `CommentRepository` |
| **Quiz** | Création batch de questions/options liées à un cours, consultation | `QuizController`, `QuestionService`, `Question/Option` |
| **Paiement** | Sauvegarde des transactions (carte, montant, méthode) exposée via `/api/payments` | `PaymentController`, `PaymentService`, `PaymentRepository` |
| **Chat interne** | Messagerie user ↔ professeur avec suivi des non-lus | `ChatController`, `ChatService`, `MessageRepository`, entité `Message` |

## 6. Modèle de données (extrait)

| Entité | But | Associations notables |
| --- | --- | --- |
| `User` (`email` PK) | Compte apprenant, attributs profil + rôle + avatar | Lien 1..N avec `Message` (sender/receiver), `Comment`, `Enrollment`, `Wishlist` |
| `Professor` (`email` PK) | Compte formateur | Relations analogues à `User`, rôle par défaut `Professor` |
| `Course` (`id` auto) | Métadonnées cours (type, langue, premium) | Référencé par `Enrollment`, `Comment`, `Chapter`, `Question` |
| `Chapter` | Décomposition du cours (jusqu’à 8 sections) | `ChapterRepository.findByCoursename` |
| `Enrollment` | Détail d’inscription (cours + inscrit + instructeur) | Stocke un snapshot des infos cours/instructeur |
| `Wishlist` | Cours favoris avec type d’utilisateur | `WishlistRepository` |
| `Comment` | Commentaires imbriqués avec rating sur un cours | `Course`, `User` ou `Professor`, auto-référence `parent/replies` |
| `Question` / `Option` | Quiz lié à un cours | `QuestionRepository.findByCourse`, `Option.question` |
| `Payment` | Transaction (montant, carte, méthode, date) | `PaymentRepository` |
| `Message` | Chat User/Professor + flags `readByUser/Professor` | Requêtes JPQL pour non-lus |

## 7. API REST principales

### Authentification & comptes

| Méthode | URI | Description |
| --- | --- | --- |
| `POST` | `/registeruser`, `/registerprofessor` | Création de compte (ID alphanumérique généré). |
| `POST` | `/login/user` | Authentifie user ou professeur, retourne JWT + profil (avec mot de passe masqué). |
| `POST` | `/auth/forgot-password` | Génère un mot de passe temporaire, envoie un email via SMTP. |
| `GET` | `/userlist`, `/professorlist` | Liste complète pour l’admin. |
| `PUT` | `/updateuser`, `/updateprofessor` | Mise à jour du profil (hash automatique des mots de passe). |
| `POST` | `/users/{email}/avatar`, `/professors/{email}/avatar` | Upload d’un avatar, renvoie l’URL publique `uploads/...`. |

### Cours, chapitres & inscriptions

| Méthode | URI | Description |
| --- | --- | --- |
| `GET` | `/listcourses`, `/api/courses`, `/api/courses/{id}` | Listing complet ou accès direct par ID. |
| `POST` | `/addCourse`, `/api/courses` | Création d’un cours côté professeur/admin. |
| `PUT` | `/api/courses/{id}` | Mise à jour complète (nom, type, langue, `isPremium`). |
| `DELETE` | `/api/courses/{id}` | Suppression via `CourseService`. |
| `POST` | `/addnewchapter` | Ajout des chapitres d’un cours. |
| `GET` | `/getchapterlistbycoursename/{coursename}` | Listing des chapitres pour l’UI. |
| `POST` | `/enrollnewcourse/{email}/{role}` | Enrôle un utilisateur (User ou Professor) dans un cours. |
| `GET` | `/getenrollmentstatus/{coursename}/{email}/{role}` | Vérifie si l’utilisateur suit déjà ce cours. |

### Wishlist, quiz, commentaires, chat, paiement

| Domaine | Routes clés |
| --- | --- |
| Wishlist | `POST /addtowishlist`, `GET /getwishliststatus/{coursename}/{email}`, `GET /getallwishlist` |
| Quiz | `POST /quiz/questions/add?courseId=COURSE_ID`, `GET /quiz/course/{courseId}/questions`, `GET /quiz/{id}` |
| Commentaires | `POST /comments`, `GET /comments/course/{courseId}`, `GET /comments/{commentId}`, `DELETE /comments/{commentId}`, `GET /comments/course/{courseId}/rating` |
| Chat | `POST /chat/user/send`, `POST /chat/professor/send`, `GET /chat?userId=&professorId=`, `GET /chat/*/unread`, `POST /chat/*/read` |
| Paiement | `POST /api/payments` (stocke la transaction, préparer l’intégration PSP ultérieure). |

> ℹ️ Tous les contrôleurs renvoient des `ResponseEntity` typés JSON. Pour les appels nécessitant un corps (ex: cours, commentaires, quiz), le frontend envoie une payload JSON conforme à l’entité/DTO (`QuestionDTO`, `OptionDTO`).

## 8. Sécurité & gouvernance des accès

- **Encodage des mots de passe** : `BCryptPasswordEncoder` pour Users/Professors. `NoOpPasswordEncoder` reste présent pour compatibilité legacy (déconseillé en prod).
- **JWT** : clé secrète Base64 (`JwtUtil.secret_key`), durée de vie 60 minutes, claims `email` + `mobile`. Le filtre `JwtAuthorizationFilter` :
  1. extrait le header `Authorization`,
  2. valide l’expiration,
  3. renseigne `SecurityContext` avec un `UsernamePasswordAuthenticationToken`.
- **Rôles** : `User` vs `Professor` (string). Certaines routes sont restreintes (ex : `/course/**` pour ADMIN/PROFESSOR). Des améliorations futures consisteraient à remplacer les `String` par un enum et à enrichir `GrantedAuthority`.
- **CORS** : origine autorisée `http://localhost:4200` (configurable via `CorsConfigurationSource`).
- **Reset password** : génération aléatoire via `SecureRandom`, envoi SMTP (paramétrage Gmail dans `application.properties`).

## 9. Flux de fonctionnement type

1. **Inscription & onboarding**
   - L’utilisateur remplit le formulaire Angular → `POST /registeruser`.
   - Le backend génère un `userid` (12 caractères), hash le mot de passe, stocke et renvoie le profil.
2. **Authentification**
   - `POST /login/user` avec email/mot de passe → `UserService.validateUser` contrôle successivement les tables `user` puis `professor`.
   - `JwtUtil` émet un token signé, retourné avec le profil (mot de passe vidé).
3. **Consommation d’un cours premium**
   - L’utilisateur paie via Angular → `POST /api/payments` pour tracer la transaction (intégration PSP à prévoir).
   - L’utilisateur est enrôlé (`POST /enrollnewcourse/...`), les compteurs sont mis à jour et l’accès au contenu premium est accordé.
4. **Quiz**
   - Le professeur crée un quiz (`POST /quiz/questions/add`) en envoyant une liste de `QuestionDTO` contenant `options`.
   - L’utilisateur récupère le quiz cours (`GET /quiz/course/{courseId}/questions`).
5. **Chat & support**
   - Messages temps réel pseudo-synchro (polling) via `ChatController`.
   - Les compteurs `/chat/*/unread` permettent d’afficher les notifications dans l’UI.
6. **Commentaires & scoring**
   - Les apprenants publient un `Comment` (option rating). Les réponses imbriquées sont gérées via `parentId`.
   - `GET /comments/course/{courseId}/rating` renvoie la moyenne affichée dans la fiche cours.

## 10. Configuration, exécution & déploiement

1. **Prérequis**
   - JDK 11, Maven 3.8+, MySQL ≥ 5.7, Node/Angular (pour le frontend).
   - Variables SMTP valides (`spring.mail.*`) pour la réinitialisation des mots de passe.
2. **Initialisation base**
   - Créer une base `elearning_db`.
   - Importer éventuellement `elearningsystem.sql` ou `elearning_db_backup.sql`.
   - Adapter `spring.datasource.*` dans `src/main/resources/application.properties`.
3. **Lancement local**
   ```bash
   cd elearning/backend/"ELearningManagement - backend"
   ./mvnw spring-boot:run
   ```
   L’API écoute sur `http://localhost:8080`.
4. **Packaging**
   ```bash
   ./mvnw clean package
   java -jar target/ELearningManagement-0.0.1-SNAPSHOT.jar
   ```
5. **Profils & paramètres**
   - `spring.jpa.hibernate.ddl-auto=update` : pratique en dev, à remplacer par des migrations (Flyway) en prod.
   - `app.upload.dir` : dossier accessible en écriture pour les avatars/documents.
   - Ajouter `SPRING_DATASOURCE_*` et `JWT_SECRET` via variables d’environnement pour déploiement cloud.

## 11. Qualité, monitoring & pistes d’amélioration

- **Tests** : Maven embarque `spring-boot-starter-test` mais aucun test n’est fourni. Il est recommandé d’ajouter des tests unitaires (services) et d’intégration (MockMvc) pour les endpoints critiques (auth, paiement, quiz).
- **Observabilité** : activer `spring-boot-starter-actuator` et configurer une stack de logs (ELK) pour suivre les inscriptions/paiements.
- **Sécurité** :
  - Remplacer `NoOpPasswordEncoder` par `BCrypt` partout.
  - Externaliser la clé JWT (`secret_key`) et définir une durée de vie configurable.
  - Ajouter un rafraîchissement de token et des rôles plus granulaires (ADMIN/USER/PROFESSOR).
- **Persistance** : introduire de vraies contraintes (FK) et des relations JPA (ex : `Enrollment` → `Course/User`) au lieu de stocker des snapshots.
- **Scalabilité** : le chat repose sur du polling. Introduire WebSocket (Spring Messaging) pour la messagerie en temps réel.

---

Pour toute contribution, respecter la structure en couches existante : nouvelle fonctionnalité → créer l’entité, le repository, le service, puis exposez un contrôleur REST en veillant à sécuriser l’accès via `SecurityConfig`. Pensez à documenter la route dans cette fiche pour faciliter la maintenance.
