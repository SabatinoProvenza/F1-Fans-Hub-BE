# 🏎️ F1 Fans Hub - Backend

Backend del progetto **F1 Fans Hub**, una piattaforma dedicata agli appassionati di Formula 1 che
unisce news, articoli salvati e una community dove gli utenti possono interagire tramite post,
commenti e like.

## 🚀 Base URL

👉 https://considerable-ilise-me-stesso-f977c3cb.koyeb.app

---

## 📌 Descrizione

Questo backend espone una **REST API** sviluppata con **Java** e **Spring Boot**, con autenticazione
tramite **JWT** e persistenza dei dati su **PostgreSQL**.

L'applicazione gestisce:

* autenticazione utenti
* recupero news F1
* salvataggio articoli preferiti
* gestione profilo utente
* pubblicazione di post nella community
* commenti e like
* funzionalità amministrative

---

## 🛠️ Tecnologie utilizzate

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* PostgreSQL

---

## 🔐 Autenticazione

L'autenticazione è gestita tramite **JWT**.

Per accedere agli endpoint protetti è necessario inviare il token nell'header `Authorization` con
questo formato:

```http
Authorization: Bearer <token>
```

---

## 📡 API Endpoints

### 🔓 Public Endpoints

#### Auth

* `POST /auth/register` → registrazione di un nuovo utente
* `POST /auth/login` → login utente e generazione del token JWT

#### News

* `GET /api/news` → recupera le news sulla Formula 1
* `GET /api/news/{guid}` → recupera il dettaglio di una news

#### Community

* `GET /post` → recupera tutti i post pubblici
* `GET /post/{postId}/comments` → recupera i commenti di un post

---

### 🔐 Protected Endpoints (Requires Authentication)

#### User

* `GET /users/me` → recupera il profilo utente autenticato
* `DELETE /users/me` → elimina l'account
* `GET /users/me/articles/{articleId}` → recupera un articolo salvato
* `PATCH /users/me/email` → aggiorna email
* `PATCH /users/me/username` → aggiorna username
* `PATCH /users/me/password` → aggiorna password
* `PATCH /users/me/image` → aggiorna immagine profilo

#### Favorites

* `POST /favorites` → aggiunge un articolo ai preferiti
* `GET /favorites` → recupera i preferiti
* `DELETE /favorites/{articleId}` → rimuove un articolo dai preferiti

#### Posts

* `POST /post` → crea un post
* `PATCH /post/{postId}` → modifica un post
* `DELETE /post/{postId}` → elimina un post
* `POST /post/{postId}/like` → aggiunge un like
* `DELETE /post/{postId}/like` → rimuove un like

#### Comments

* `POST /post/{postId}/comments` → aggiunge un commento
* `PATCH /comments/{commentId}` → modifica un commento
* `DELETE /comments/{commentId}` → elimina un commento

---

### 🛡️ Admin Endpoints

#### Posts

* `GET /admin/posts` → recupera tutti i post
* `DELETE /admin/posts/{postId}` → elimina un post
* `GET /admin/posts/{postId}/comments` → commenti di un post

#### Comments

* `GET /admin/comments` → recupera tutti i commenti
* `DELETE /admin/comments/{commentId}` → elimina un commento

---

## 🗄️ Database

Il database è strutturato con le seguenti tabelle:

* `users`
* `articles`
* `favorites`
* `post`
* `comments`
* `likes`

---

## 🔗 Repository collegati

* Frontend: https://github.com/SabatinoProvenza/F1-Fans-Hub-FE
* Backend: https://github.com/SabatinoProvenza/F1-Fans-Hub-BE

---

## 🎯 Obiettivo del progetto

Fornire un backend robusto per una piattaforma dedicata alla Formula 1 che unisca:

* aggiornamenti e news
* personalizzazione dei contenuti
* interazione tra utenti tramite community

---

## 👤 Autore

**Sabatino Provenza**


