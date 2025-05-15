## Components and Responsibilities

### 1. Frontend (React)

**Responsibilities:**

- Provide a chat-based UI for interaction.
- Handle authentication with GitLab LRZ via OAuth2/OpenID.
- Allow users to input prompts and view generated responses.
- Let users manage and update their dietary preferences.
- Display recipe history and allow revisiting past conversations.

**Core Features:**

- Login with GitLab LRZ SSO
- Chat interface (user prompt + LLM response)
- Preference settings (gluten-free, diabetic, vegan, etc.)
- Recipe history viewer

---

### 2. Backend (Spring Boot REST API)

**Responsibilities:**

- Authenticate and authorize users via GitLab LRZ.
- Manage user profiles and dietary preferences.
- Route prompts to the GenAI service and handle responses.
- Store and retrieve chat and recipe history from MongoDB.
- Expose REST endpoints for client-side operations.

---

### 3. GenAI Microservice (Python + LangChain)

**Responsibilities:**

- Process incoming prompts and preferences.
- Use LLM (e.g., GPT via LangChain) to generate, modify, and plan recipes.
- Structure outputs into JSON responses for easy consumption by the backend.
- Fetch additional data from a well known recipe source stored in the vector database.

**Design:**

- LangChain chains/tools for recipe generation and transformation
- Prompt templates that incorporate user preferences and dietary constraints

---

### 4. Database (MongoDB)

**Collections:**

- `user-preferences` – Stores user data and dietary preferences
- `chats` – Stores user messages and GenAI responses

**Data Considerations:**

- Preferences are indexed for fast lookup

---

### 5. Authentication (GitLab LRZ SSO)

**Flow:**

- React initiates OAuth2 login with GitLab LRZ
- GitLab redirects with authorization code
- Spring Boot backend exchanges code for tokens
- Backend creates or updates the user profile in MongoDB
- Tokens used for secure communication between frontend and backend

---

## Communication Flow Example

1. User logs in via GitLab LRZ → token returned.
2. User types: _"Suggest a vegan dinner with lentils."_
3. React sends prompt + preferences to Spring Boot API.
4. API calls GenAI microservice with combined data.
5. GenAI returns structured recipe.
6. API stores recipe + chat history in MongoDB.
7. Response returned to frontend and rendered in chat UI.

---

## Technologies

| Component       | Technology        |
| --------------- | ----------------- |
| Frontend        | React, TypeScript |
| Backend         | Spring Boot, Java |
| GenAI Service   | Python, LangChain |
| Database        | MongoDB           |
| Vector Database | Qdrant            |
| Auth            | GitLab LRZ SSO    |
