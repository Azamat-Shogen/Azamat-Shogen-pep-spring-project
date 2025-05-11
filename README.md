# 🚀 Spring Social Media Blog API

---

## 📝 Background

Full-stack applications typically involve a **front end** for user interaction and a **backend** for managing persistent data.

This project focuses on building the **backend** for a hypothetical social media application. It manages user accounts and their posted messages using the powerful **Spring Framework** for Java. Spring enables automatic dependency injection and configuration for various features, including data persistence, API endpoints, and standard data manipulation logic (CRUD operations).

In this micro-blogging or messaging application, users can view all messages or messages posted by a specific user. The backend is responsible for delivering this data and processing actions such as user registration, login, message creation, updating, and deletion.

---

## 💾 Database Tables

The following tables will be automatically initialized in the project's embedded database upon startup. This is configured in the `application.properties` file using the provided SQL script.

### 👤 Account

```sql
CREATE TABLE Account (
    accountId INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255)
);
```
## 💬 Message

```sql
CREATE TABLE Message (
messageId INTEGER PRIMARY KEY AUTO_INCREMENT,
postedBy INTEGER,
messageText VARCHAR(255),
timePostedEpoch BIGINT,
FOREIGN KEY (postedBy) REFERENCES Account(accountId)
);
```

## 🌱 Spring Technical Requirements

This project **must** leverage the **Spring Boot Framework**.

While Java classes are provided, the entire project's functionality **must** be implemented using Spring. In addition to functional tests, "**SpringTest**" will verify the proper utilization of the Spring framework, including **Spring Boot**, **Spring MVC**, and **Spring Data**.

**SpringTest will verify the following:**

* That beans exist for `AccountService`, `MessageService`, `AccountRepository`, `MessageRepository`, and `SocialMediaController`.
* That `AccountRepository` and `MessageRepository` are functional `JpaRepository` interfaces based on the `Account` and `Message` entities.
* That the Spring Boot application leverages MVC by checking for Spring's default error message structure.

The project starts as a valid Spring Boot application with a configured `application.properties` and valid database entities.

## 📜 User Stories

### 1️⃣ User Registration

**As a user, I should be able to create a new Account on the endpoint `POST localhost:8080/register`. The request body will contain a JSON representation of an Account (without `accountId`).**

* ✅ **Successful Registration:** If the username is not blank, the password is at least 4 characters long, and the username is unique, the response body should contain a JSON representation of the created `Account` (including its `accountId`), and the response status should be `200 OK`. The new account should be persisted in the database.
* ❌ **Duplicate Username:** If an account with the provided username already exists, the response status should be `409 Conflict`.
* ❌ **Client Error:** For any other registration failure (e.g., blank username, short password), the response status should be `400 Bad Request`.

**Example Request Body:**

```json
{
  "username": "newuser",
  "password": "securepassword"
}
```
**Example Successful Response Body:**

```json
{
"accountId": 123,
"username": "newuser",
"password": "securepassword"
}
```

### 2️⃣ User Login

**As a user, I should be able to verify my login on the endpoint `POST localhost:8080/login`. The request body will contain a JSON representation of an Account (with username and password).**

* ✅ **Successful Login:** If the provided username and password match an existing account in the database, the response body should contain a JSON representation of the logged-in `Account` (including its `accountId`), and the response status should be `200 OK`.
* ❌ **Unauthorized:** If the login credentials are incorrect, the response status should be `401 Unauthorized`.

**Example Request Body:**
```json
{
  "username": "existinguser",
  "password": "correctpassword"
}
```
**Example Successful Response Body:**
```json
{
  "accountId": 456,
  "username": "existinguser",
  "password": "correctpassword"
}
```

### 3️⃣ Create New Message

**As a user, I should be able to submit a new post on the endpoint `POST localhost:8080/messages`. The request body will contain a JSON representation of a Message (without `messageId`), which should be persisted to the database.**

* ✅ **Successful Creation:** If the `messageText` is not blank, not over 255 characters, and the `postedBy` refers to a valid existing user's `accountId`, the response body should contain a JSON representation of the created `Message` (including its `messageId`), and the response status should be `200 OK`. The new message should be persisted in the database.
* ❌ **Client Error:** If the message creation fails (e.g., blank or too long `messageText`, invalid `postedBy`), the response status should be `400 Bad Request`.

**Example Request Body:**
```json
{
  "postedBy": 456,
  "messageText": "Hello Spring Social Media!"
}
```
**Example Successful Response Body:**
```json
{
  "messageId": 789,
  "postedBy": 456,
  "messageText": "Hello Spring Social Media!",
  "timePostedEpoch": 1678886400000
}
```

## 4️⃣ Retrieve All Messages

**As a user, I should be able to submit a GET request on the endpoint: 
`GET localhost:8080/messages`**

**✅ Successful Retrieval:**

* The response body should contain a JSON array containing all messages retrieved from the database.
* If there are no messages, the array should be empty (`[]`).
* The response status should always be `200 OK`.

**Example Response Body:**

```json
[
  {
    "messageId": 789,
    "postedBy": 456,
    "messageText": "Hello Spring Social Media!",
    "timePostedEpoch": 1678886400000
  },
  {
    "messageId": 901,
    "postedBy": 123,
    "messageText": "Another message!",
    "timePostedEpoch": 1678886500000
  }
]
```
## 5️⃣ Retrieve Message by ID

**As a user, I should be able to submit a GET request on the endpoint:**
`GET localhost:8080/messages/{messageId}`

**✅ Successful Retrieval:**

* The response body should contain a JSON representation of the message identified by the `{messageId}` path parameter.
* If no such message exists, the response body should be empty (`{}`).
* The response status should always be `200 OK`.

**Example Response Body (for messageId `789`):**

```json
{
  "messageId": 789,
  "postedBy": 456,
  "messageText": "Hello Spring Social Media!",
  "timePostedEpoch": 1678886400000
}
```

## 6️⃣ Delete Message by ID

**As a User, I should be able to submit a DELETE request on the endpoint:**
`localhost:8080/messages/{messageId}.`

**✅ Successful Deletion (Message Existed):**

* If a message with the given `{messageId}` existed and was deleted, the response body should contain the number of rows updated (`1`).
* The response status should be `200 OK`.

**✅ Idempotent Deletion (Message Did Not Exist):**

* If no message with the given `{messageId}` existed, the response status should still be `200 OK`.
* The response body should be empty (`{}`).

**Example Successful Response Body (Message Existed):**

```json
1
```
Example Response Body (Message Did Not Exist):

_(Empty Body)_

### 7️⃣ Update Message Text by ID

**As a user, I should be able to submit a PATCH request on the endpoint:**
`PATCH localhost:8080/messages/{messageId}`

**The request body should contain a JSON object with the new `messageText` value.**

**✅ Successful Update:** If a message with the given `{messageId}` exists, and the new `messageText` is not blank and not over 255 characters, the response body should contain the number of rows updated (`1`), and the response status should be `200 OK`. The message in the database should be updated with the new text.

**❌ Client Error:** If the update fails (e.g., invalid `{messageId}`, blank or too long `messageText`), the response status should be `400 Bad Request`.

**Example Request Body:**
```json
{
  "messageText": "Updated message content!"
}
```

**Example Successful Response Body:**
```json
1
```
### 8️⃣ Retrieve Messages by User ID

**As a user, I should be able to submit a GET request on the endpoint:**
`GET localhost:8080/accounts/{accountId}/messages`

**✅ Successful Retrieval:** The response body should contain a JSON array containing all messages posted by the user with the given `{accountId}`. If the user has no messages, the array should be empty (`[]`). The response status should always be `200 OK`.

**Example Response Body:**

```json
[
  {
    "messageId": 789,
    "postedBy": 456,
    "messageText": "First post by this user.",
    "timePostedEpoch": 1678886400000
  },
  {
    "messageId": 901,
    "postedBy": 456,
    "messageText": "Another thought.",
    "timePostedEpoch": 1678886500000
  }
]
```

### 9️⃣ Spring Framework Utilization

**✅ The project has been developed using the Spring Framework, incorporating dependency injection, autowiring, and/or Spring annotations throughout the codebase.**

## 👥 Contributors

[Azamat Shogen](https://github.com/Azamat-Shogen)

## 📄 License

This project is licensed under the [MIT License](LICENSE) – see the LICENSE file for details.

## 📚 Documentation

For detailed architecture and design decisions, see the [project wiki](https://example.com/wiki).



