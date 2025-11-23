# Documentation technique - Plateforme eLearning

Ce document decrit l architecture technique du projet (backend Spring Boot + frontend Angular), les principaux modules, les endpoints exposes et les pre-requis d execution.

## 1. Stack et structure
- Backend : Java 11, Spring Boot 2.x, Spring MVC, Spring Data JPA (MySQL), Spring Security (JWT + OAuth2), Mail (JavaMailSender).
- Frontend : Angular (SPA) servie sur http://localhost:4200.
- Build : Maven wrapper (`mvnw`), Node/NPM pour le front.
- Arborescence backend : `configuration/` (security, JWT, CORS), `controller/` (REST), `services/` (metier), `repository/` (DAO Spring Data), `model/` (entites + DTO), `resources/` (config).

## 2. Configuration cle (application.properties)
- Port API : 8080.
- Base : `spring.datasource.url=jdbc:mysql://localhost:3306/elearning_db`, user `root`, pass `123456789`.
- JPA : `spring.jpa.hibernate.ddl-auto=update`, dialect MySQL55, `spring.jpa.show-sql=true`.
- SMTP : host smtp.gmail.com, port 587, username/password a externaliser en variables d environnement; option `app.mail.from` via @Value.
- Uploads : `app.upload.dir=uploads` (servi par `WebConfig`).
- OAuth2 : placeholders pour Google/GitHub/LinkedIn (`spring.security.oauth2.client.registration.*`), redirections vers le front (`app.oauth2.frontend-success/error`).

## 3. Securite
- `SecurityConfig` : CSRF desactive (SPA), CORS autorise http://localhost:4200, filtrage JWT via `JwtAuthorizationFilter` place avant `UsernamePasswordAuthenticationFilter`.
- Routes publiques : `/login/**`, `/auth/**`, `/register**`, `/professors/**`, `/users/**`, `/quiz/**`, `/addCourse`, `/email-exists`, `/verify-email`, `/listcourses`.
- Le reste requiert un header `Authorization: Bearer <token>` valide (genere par `JwtUtil` au login).
- Mot de passe : BCrypt via `PasswordConfig`.
- OAuth2 : handlers succes/echec (`OAuth2LoginSuccessHandler`, `OAuth2LoginFailureHandler`) + `CustomOAuth2UserService`.

## 4. Modeles et repositories
- Entites principales : `User`, `Professor`, `Course`, `Chapter`, `Enrollment`, `Wishlist`, `Comment` (parent/replies, rating), `Question`/`Option` (quiz), `Payment`, `Message` (chat).
- Repositories Spring Data : interfaces CRUD avec finders par email, cours, non-lus, etc.

## 5. Services metier
- Auth/identite : `CustomUserDetailsService`, `UserService` (login, premium, profil), `ProfessorService` (statut, profil), `EmailService` (reset/creation compte).
- Contenu : `CourseService`, `ChapterService` (CRUD, recherche).
- Quiz : `QuizService`, `QuestionService` (batch creation de questions/options).
- Engagement : `EnrollmentService`, `WishlistService`, `CommentService` (rating moyen), `ChatService` (messages + non-lus).
- Paiement : `PaymentService` (trace transaction).

## 6. Endpoints REST (principaux)
- Auth/inscription : `POST /registeruser`, `POST /registerprofessor`, `GET /email-exists`, `GET /verify-email`, `POST /login/user`, `POST /auth/forgot-password`.
- Users/Admin : `GET /userlist`, `GET /users`, `GET /userprofileDetails/{email}`, `PUT /updateuser`, `PUT /users/{email}`, `PUT /user/premuim/{email}/{isPremuim}`, `DELETE /user/{id}`, `DELETE /users/{email}`, upload avatar `/users/{email}/avatar`, stats `/gettotalusers`, `/gettotalenrollments`, `/gettotalenrollmentcount`.
- Professeurs : `GET /professorlist`, `GET /professors`, `GET /professorprofileDetails/{email}`, `GET /professorlistbyemail/{email}`, `GET /listcourses`, stats `/gettotalprofessors`/`gettotalcourses`/`gettotalchapters`/`gettotalwishlist`, `POST /addProfessor`, `POST /addCourse`, `POST /addnewchapter`, `PUT /updateprofessor`, `PUT /professors/{email}`, `DELETE /professor/{id}`, `DELETE /professors/{email}`, avatar `/professors/{email}/avatar`, statut `/acceptstatus/{email}`, `/rejectstatus/{email}`.
- Cours/chapitres (REST standard) : `/api/courses` POST/GET/PUT/DELETE, `/api/courses/{id}`, recherche `/api/courses/search/byName`, listing legacy `/listcourses`.
- Inscriptions/wishlist : `POST /enrollnewcourse/{email}/{role}`, `GET /getenrollmentstatus/{coursename}/{email}/{role}`, `GET /getenrollmentbyemail/{email}/{role}`, `POST /addtowishlist`, `GET /getwishliststatus/{coursename}/{email}`, `GET /getallwishlist`, `GET /getwishlistbyemail/{email}`.
- Quiz : `POST /quiz/questions/add?courseId=...` (liste QuestionDTO + options), `GET /quiz/course/{courseId}/questions`, `GET /quiz`, `GET /quiz/{id}`.
- Commentaires : `POST /comments`, `GET /comments/course/{courseId}`, `GET /comments/replies/{parentId}`, `GET /comments/user/{userId}`, `GET /comments/professor/{professorId}`, `GET /comments/{commentId}`, `DELETE /comments/{commentId}`, moyenne `/comments/course/{courseId}/rating`.
- Chat : `POST /chat/user/send`, `POST /chat/professor/send`, `GET /chat?userId=&professorId=`, `GET /chat/professor/unread`, `GET /chat/user/unread`, marquer lu `/chat/professor/read`, `/chat/user/read`.
- Paiement : `POST /api/payments` (persist transaction).

## 7. DTO & formats (exemples)
- Auth : email, password -> reponse profil + token JWT; password reset envoie un mot de passe temporaire par email.
- Quiz : `QuestionDTO` contient intitulé, description, options: `OptionDTO` (texte, isCorrect).
- Commentaire : JSON avec `content`, `rating` (optionnel), `courseId`, `parentId` possible.
- Upload avatar : `multipart/form-data` avec champ `file`, retour URL `/uploads/...`.
- Paiement : JSON avec montant, methode, cardNumber masque, holderName, email.

## 8. Flux metier clefs
1) Inscription -> verification email (`/verify-email`) -> login -> token JWT.
2) Acces premium : POST `/api/payments` (trace) -> `/enrollnewcourse/...` -> cours premium accessible.
3) Quiz : prof cree via `/quiz/questions/add` -> apprenant lit via `/quiz/course/{id}/questions`.
4) Chat : envoi user/prof, polling unread, marquage lu.
5) Commentaires/rating : publication puis moyenne via `/comments/course/{courseId}/rating`.

## 9. Execution locale
- Prerequis : JDK 11, Maven 3.8+, MySQL, Node 18+.
- Base : creer `elearning_db`, adapter `spring.datasource.*` et SMTP.
- Backend : `cd elearning/backend/"ELearningManagement - backend"` puis `./mvnw spring-boot:run`.
- Frontend : `cd elearning/frontend`, `npm install`, `npm start` (http://localhost:4200).

## 10. Points d amelioration
- Externaliser secrets (DB, SMTP, OAuth2, JWT), utiliser profiles Spring.
- Remplacer `ddl-auto=update` par migrations Flyway.
- Ajouter Bean Validation sur DTO et un ControllerAdvice pour les erreurs homogènes.
- Tests : unitaires services, integration MockMvc (auth, cours, paiement, quiz).
- Chat en WebSocket si besoin temps reel, roles/authorities via enum.

