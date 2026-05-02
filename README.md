# Dash Backend (Spring Boot + PostgreSQL)

## Prerequisites
- Java 17+
- PostgreSQL 14+
- Gradle (or use your local wrapper setup)
- Docker (optional, for containerized deployment)

## Configure database
Update credentials in `src/main/resources/application.yml`:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

Create database and user example:

```sql
CREATE DATABASE dash_db;
CREATE USER dash_user WITH ENCRYPTED PASSWORD 'dash_password';
GRANT ALL PRIVILEGES ON DATABASE dash_db TO dash_user;
```

## Run
From the `backend` folder:

```bash
gradle bootRun
```

## Docker deployment

### 1) Build backend image
From `backend` folder:

```bash
docker build -t dash-backend:latest .
```

### 2) Run backend container (external PostgreSQL)

```bash
docker run --name dash-backend \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<db-host>:5432/dash_db \
  -e SPRING_DATASOURCE_USERNAME=dash_user \
  -e SPRING_DATASOURCE_PASSWORD=dash_password \
  dash-backend:latest
```

### 3) Run backend + PostgreSQL together with Docker Compose

```bash
docker compose up -d --build
```

Stop containers:

```bash
docker compose down
```

With volume cleanup:

```bash
docker compose down -v
```

### Environment variables used by backend
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SERVER_PORT` (default: `8080`)

## Endpoints
- `GET /api/v1/health`
- `POST /api/v1/sync/keystrokes`
- `POST /api/v1/sync/app-usage`
- `POST /api/v1/sync/locations`

## Example payloads
`POST /api/v1/sync/keystrokes`

```json
{
  "childExternalId": "device-001",
  "payload": "typed text",
  "capturedAt": "2026-05-02T10:15:30Z"
}
```

`POST /api/v1/sync/app-usage`

```json
{
  "childExternalId": "device-001",
  "payload": "com.instagram.android:opened",
  "capturedAt": "2026-05-02T10:15:30Z"
}
```

`POST /api/v1/sync/locations`

```json
{
  "childExternalId": "device-001",
  "latitude": 33.5731,
  "longitude": -7.5898,
  "capturedAt": "2026-05-02T10:15:30Z"
}
```
