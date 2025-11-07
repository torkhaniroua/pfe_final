This repository is a two-tier Java Spring Boot backend + Angular frontend application.

Quick summary (what matters to an AI coding agent):
- Backend: Spring Boot app located in `backend/ELearningManagement - backend/` (Maven project).
- Frontend: Angular 12 app in `frontend/` (Angular CLI, `defaultProject: ElearningManagement`).
- Runtime config: backend `src/main/resources/application.properties` contains DB and server.port (defaults: 8080, MySQL jdbc:mysql://localhost:3306/elearning_db).

Essential commands (where to run them)
- Frontend (from `frontend/`):
  - Install: `npm install`
  - Dev server: `npm run start` (package.json uses `set NODE_OPTIONS=--openssl-legacy-provider && ng serve` — this is Windows CMD syntax). In PowerShell you can set the env var then run the CLI:
    - PowerShell: `$env:NODE_OPTIONS='--openssl-legacy-provider'; ng serve`
    - Or run in cmd: `cmd /c "set NODE_OPTIONS=--openssl-legacy-provider && npm run start"`
  - Build (production): `npm run build` -> produces `dist/ElearningManagement` per `angular.json`.
  - Tests: `npm test` (Karma/Jasmine; `karma.conf.js` present).

- Backend (from `backend/ELearningManagement - backend/`):
  - Using included wrapper (Windows): `.
    mvnw.cmd spring-boot:run` from that directory, or run `mvn spring-boot:run` if Maven is installed.
  - Tests: `mvn test` or `.
    mvnw.cmd test`.

Key configuration and patterns to know
- Database & server: `src/main/resources/application.properties` sets `server.port=8080` and the MySQL connection (`elearning_db`, user `root`, password `123456789`). Many integration/debugging tasks require adjusting or mocking this file.
- Security & auth: The backend includes Spring Security + JWT dependencies (see `pom.xml`) — expect authentication filters, token handling and protected endpoints. Be careful changing endpoints or controller mappings without checking security config.
- Frontend build specifics: `angular.json` lists styles and global scripts (e.g., jQuery and Bootstrap included via `scripts` and `styles`). This means some components assume global CSS/JS present (look in `src/app/layouts/` and `src/assets/`). When editing components, verify style/script order in `angular.json`.
- Environment files: `src/environments/environment.ts` and `environment.prod.ts` are used for API base URLs — update them when changing backend ports or base paths.

Project structure highlights (use these paths when searching or making edits)
- Frontend main app: `frontend/src/app/` — major component groups: `components/`, `guards/`, `layouts/`, `services/`, `models/`.
  - Example: authentication interceptor is at `src/app/auth.interceptor.ts` and guards under `src/app/guards/`.
- Backend main: `backend/ELearningManagement - backend/src/main/java/` (package `com.application` per `pom.xml`). Look for controllers, services and repositories here.
- Backend resources: `backend/.../src/main/resources/application.properties` (DB, port), plus other static templates if present.

Conventions and gotchas for code edits
- Don't assume a modern Node runtime: Angular 12 + TypeScript ~4.2 and `@angular/cli` 12 require compatible Node; if CI or local dev fails, check Node version (Node 14.x is a safe choice for Angular 12).
- The frontend `start` script uses `set ... && ng serve` — this is Windows CMD syntax and may break in PowerShell or non-Windows CI. If you change scripts, prefer `cross-env` for cross-platform env vars.
- Many UI components rely on global JS (jQuery/bootstrap) injected from `angular.json` — migrating those to Angular-native solutions may require refactoring multiple components under `src/app/layouts/` and `src/app/components/`.
- Backend uses Lombok (provided scope) and Spring Boot DevTools; code edits that change Lombok annotations may require a rebuild or IDE Lombok plugin for compilation.

How to run tests and quick verification
- Frontend quick smoke: from `frontend/` run `npm install` then `npm run build`. Serve `dist/ElearningManagement` or run `npm run start` and visit `http://localhost:4200`.
- Backend quick smoke: start MySQL (or change `application.properties` to use an in-memory DB), then run `.
  mvnw.cmd spring-boot:run` and visit `http://localhost:8080/actuator/health` (if actuator present) or API endpoints.

When making changes, search these files first
- `frontend/package.json` — npm scripts & dependencies
- `frontend/angular.json` — build config, assets, scripts
- `frontend/src/environments/*` — API base URLs used by services
- `backend/.../pom.xml` — dependencies (JWT, Lombok, Spring Security)
- `backend/.../src/main/resources/application.properties` — ports and DB

Safety notes for the AI agent
- Preserve existing environment-sensitive scripts unless you update both documentation and CI (e.g., replacing `set` with `cross-env`).
- When changing backend API paths, update frontend service calls under `frontend/src/app/services/` and environment files.

If you need clarification or want me to expand any section (example edits, cross-platform script fixes, or adding CI steps), tell me which area to improve.
