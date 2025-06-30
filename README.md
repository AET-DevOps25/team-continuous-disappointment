# RecipAI
RecipAI is available at [`https://recipai.student.k8s.aet.cit.tum.de`](https://recipai.student.k8s.aet.cit.tum.de)

## Project Description

### Main Functionality

The primary functionality of the application is to enable users to generate and explore recipes through a natural language interface powered by a LLM. The application allows users to search for recipes based on ingredients, generate meal plans, and receive step-by-step cooking instructions tailored to their preferences and dietary requirements.

### Intended Users

The application is designed for home cooks, culinary enthusiasts, individuals with dietary restrictions, and anyone interested in exploring or generating recipes in an intuitive and flexible manner. It is particularly useful for users who wish to interact with a recipe database using natural language queries.

### Integration of Generative AI

Generative AI is integrated meaningfully through a dedicated LLM microservice developed in Python. This service processes user inputs in natural language, generates recipes based on the provided ingredients, modifies existing recipes according to user needs, and provides meal suggestions. The use of GenAI enhances the user experience by offering creative, context-aware, and highly adaptable culinary solutions.

### Functional Scenarios

1. **Ingredient-Based Recipe Generation**: A user inputs, "Suggest a quick dinner recipe with chicken and broccoli." The system uses the LLM to generate a relevant recipe, which is presented to the user through the user interface.

2. **Recipe Modification**: A user submits a traditional recipe and requests, "Make this vegan." The LLM identifies non-vegan ingredients and substitutes them with plant-based alternatives, returning a modified version of the recipe.

3. **Meal Planning**: A user asks for a weekly meal plan. The LLM generates a diverse and nutritionally balanced plan, optionally based on dietary restrictions or cuisine preferences.

4. **Ingredient-Limited Cooking**: A user specifies available ingredients, such as "eggs, spinach, and cheese," and the system suggests recipes that can be prepared using those ingredients, optimizing for simplicity and flavor.

## Components and Responsibilities

### 1. Frontend (React)

***Responsible Students:*** 
- Mehmed Esad Akcam & Ege Dogu Kaya

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


### 2. Backend (Spring Boot REST API)

***Responsible Student:*** 
- Mehmed Esad Akcam

**Responsibilities:**

- Authenticate and authorize users via GitLab LRZ.
- Manage user profiles and dietary preferences.
- Route prompts to the GenAI service and handle responses.
- Store and retrieve chat and recipe history from MongoDB.
- Expose REST endpoints for client-side operations.


### 3. GenAI Microservice (Python with FastAPI + LangChain + Qdrant)

***Responsible Student:*** 
- Ege Dogu Kaya

**Responsibilities:**

- Process incoming prompts and preferences.
- Use LLM (e.g., GPT via LangChain) to generate, modify, and plan recipes.
- Structure outputs into JSON responses and provide endpoints via FastAPI for server module.
- Fetch additional data from a well known recipe source stored in the vector database Qdrant.

**Design:**

- LangChain chains/tools for recipe generation and transformation
- Prompt templates that incorporate user reqeusts, preferences and dietary constraints
- Qdrant vector database to store embedded documents for similarity search


### 4. Database (MongoDB)

**Collections:**

- `user-preferences` – Stores user data and dietary preferences
- `chats` – Stores user messages and GenAI responses

**Data Considerations:**

- Preferences are indexed for fast lookup


### 5. Vector Database (Qdrant)

**Collections:**

- `recipes` – Stores embedded recipe documents which are uploaded by the user


### 6. DevOps

***Responsible Students:*** 
- Mehmed Esad Akcam & Ege Dogu Kaya

***Responsibilites***
- Each service is dockerized and has its own image in github registry
- CI/CD pipelines to deploy on student cluster in Rancher and AWS
- Helm charts for k8s deployment
- Docker compose for AWS deployment


### 7. Authentication (GitLab LRZ SSO)

**Flow:**

- React initiates OAuth2 login with GitLab LRZ
- GitLab redirects with authorization code
- Spring Boot backend exchanges code for tokens
- Backend creates or updates the user profile in MongoDB
- Tokens used for secure communication between frontend and backend


### Communication Flow Example

1. User logs in via GitLab LRZ → token returned.
2. User types: _"Suggest a vegan dinner with lentils."_
3. React sends prompt + preferences to Spring Boot API.
4. API calls GenAI microservice with combined data.
5. GenAI returns structured recipe.
6. API stores recipe + chat history in MongoDB.
7. Response returned to frontend and rendered in chat UI.


### Technologies

| Component       | Technology        |
| --------------- | ----------------- |
| Frontend        | React, TypeScript |
| Backend         | Spring Boot, Java |
| GenAI Service   | Python, LangChain |
| Database        | MongoDB           |
| Vector Database | Qdrant            |
| Auth            | GitLab LRZ SSO    |


## Setup Instructions

### Prerequisites

- Node.js (v22 or later)
- Java JDK 21+
- Python 3.11
- Gradle
- Docker and Docker Compose
- Git
- Kubernetes and Helm (for Kubernetes deployment)

### Clone the Repository

```bash
git clone https://github.com/AET-DevOps25/team-continuous-disappointment.git
cd team-continuous-disappointment
```

### Client Setup

1. Navigate to the `client` directory:
   ```bash
   cd client
   ```
2. Install dependencies:
   ```bash
   npm install
   ```

### Server Setup

1. Navigate to the `server` directory:
   ```bash
   cd server
   ```
2. Build the project:
   ```bash
   ./gradlew build
   ```

### GenAI Service Setup

1. Navigate to the `llm` directory:
   ```bash
   cd genai
   ```
2. Install dependencies:
   ```bash
   python3 -m venv .venv
   source .venv/bin/activate
   pip3 install -r requirements.txt
   ```

## Running the Application

### Start the Databases

```bash
docker compose -f docker-compose-dev.yml up -d
```

### Start the Client

```bash
cd client
npm run dev
```
The client will be available at [http://localhost:5173](http://localhost:5173).

### Start the Server Services
```bash
cd server
```
#### API-Gateway Service
```bash
./gradlew :api-gw:bootRun
```
The API-Gateway Service API will be available at [http://localhost:8080](http://localhost:8080).

#### User Service
```bash
./gradlew :user:bootRun
```
The User Service API will be available at [http://localhost:8081](http://localhost:8081).

#### Chat Service
```bash
./gradlew :chat:bootRun
```
The Chat Service API will be available at [http://localhost:8082](http://localhost:8082).

### Start the GenAI Service

```bash
cd genai
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```
The LLM service will be available at [http://localhost:8000](http://localhost:8000).

## Development Workflow

### Client Development

- Built with React + Vite and TypeScript for a modern, reactive UI.
- TailwindCSS for styling.
- Components, routes, pages, hooks, types, and services are organized in the `client/src` directory.
- Features user preferences, file uploading, and a modern chat with history.

### Server Development

- Built with Spring Boot for scalable and maintainable server services.
- Includes REST communication with the GenAI service.
- MongoDB integration for user preferences and chat history storage.
- RESTful APIs for user and chat management, with a unified entry point for all services.
- Gradle is used for dependency management and building.
- Source code is in the `server/src/main/java` directory.
- Tests are in the `server/src/test/java` directory.

### GenAI Service Development

- Built with FastAPI for AI-powered recipe recommendations.
- Integrates with local and cloud LLMs for generating suggestions based on the given ingredients.
- Stores embedded documents in a vector database to be able to make similarity search.
- Source code is in the `genai` directory.
- Tests are in the `genai/tests` directory.

#### GenAI Usage
- The GenAI service is responsible for all interactions with the language model used to generate and modify recipes in RecipAI. It receives user inputs as free-text prompts and responds with structured outputs such as complete recipes, meal plans, or modified instructions. It is implemented using FastAPI, LangChain, and integrates with local and cloud large language models.

##### Retrieval-Augmented Generation
- The GenAI service uses Qdrant as a vector store to retrieve relevant documents before querying the LLM. It adds the retrieved context to the prompt to improve the relevance of answers.

##### Integration
- The client UI sends user requests to the server, which forwards them to the GenAI service along with the user’s query and chat history to support multi-turn conversations. GenAI service then makes a similarity search in the vector database with the given query, and generates a respective answer. GenAI service is able to provide a proper answer altough no similar context is found in the vector database. (Endpoint: POST - `genai/generate`)
- If the user wants to upload a recipe file, client UI sends the file content directly to the GenAI service, where the content of the file is chunked, embedded, and stored in the vector database. (Endpoint: POST - `genai/upload`)

##### Vector Database - Qdrant
We use Qdrant as the vector database to enable semantic search and retrieval-augmented generation (RAG) in RecipAI. Embeddings are generated using OpenAI’s small embedding model `text-embedding-3-small`.

```bash
# Example: Creating OpenAI embeddings for ingestion
embeddings = OpenAIEmbeddings(
    model="text-embedding-3-small",
    openai_api_key=Config.api_key_openai
)
```

##### Environment Variables
- If you want to use cloud and local based LLM models, you need to set the respective api key in your `.env` file. Required `.env` variables:
```bash
# Cloud based LLM models
API_OPENAI="your openai key"
API_ANTHROPIC="your anthropic key"
API_MISTRAL="your mistral key"
API_HUGGINGFACEHUB="your huggingface api token"
# Local Models
API_OPENWEBUI="your openwebui key"
# Base URL for calling local models
BASE_URL="base url where openwebui is hosted"
```

- Example for Cloud LLM Models (defined in `genai/routes/routes.py`):
```bash
llm_cloud_anthropic = CloudLLM(
     model_name="claude-3-sonnet-20240229",
     model_provider="anthropic",
     api_key=Config.api_key_anthropic,
 )
 llm_cloud_openai = CloudLLM(
     model_name="gpt-4-1106-preview",
     model_provider="openai",
     api_key=Config.api_key_openai,
 )

 llm_cloud_mistral = CloudLLM(
     model_name="mistral-medium",
     model_provider="mistral",
     api_key=Config.api_key_mistral,
 )

 # If no parameters are provided, the default cloud model will be openai.

# Example:
# llm = CloudLLM() # same as llm_cloud_openai
```

- Example for Local LLM Models (defined in `genai/routes/routes.py`):

```bash
# Make calls to local models in openwebui (model can be changed by giving a different model_name as parameter)
llm = ChatModel(model_name="llama3.3:latest")
```

## Building for Production

### Client Build

```bash
cd client
npm run build prod
```

### Server Build

```bash
cd server
./gradlew clean build
```

## Dockerized Deployment

The project includes Docker configurations for containerized deployment.

### Build and Run with Docker Compose

1. Build and start all services:
   ```bash
   docker compose -f docker-compose.yml up --build -d
   ```
2. Access the application:
   - Client: [http://localhost:3000](http://localhost:3000)
   - API Gateway Service: [http://localhost:8080](http://localhost:8080)
   - User Service: [http://localhost:8081](http://localhost:8081)
   - Chat Service: [http://localhost:8082](http://localhost:8082)
   - GenAI Service: [http://localhost:8000](http://localhost:8000)
   - Database: MongoDB on port 27017, Mongo Express is accessible at [http://localhost:8083](http://localhost:8083)
   - Vector Database: Qdrant on port 6333, Web UI is accessible at [http://localhost:6333/dashboard](http://localhost:6333/dashboard)
   - Grafana Dashboard: [http://localhost:3001](http://localhost:3001)
   - Prometheus: [http://localhost:9090](http://localhost:9090)
   - Loki runs on port 3100
   - Cadvisor runs on port 8003

## Kubernetes Deployment

The project includes Helm charts for Kubernetes deployment in the `recipai-chart` directory.

### Deploy with Helm

1. Install the Helm chart:
   ```bash
   helm -n <your namespace> install recip-ai ./recipai-chart \
            --set secrets.gitlabClientSecret="your gitlab client secret" \
            --set secrets.mongodbAdminPassword="your mongodb admin password" \
            --set secrets.apiOpenAi="your open ai api key" \
            --set secrets.apiOpenWebUi="your open web ui api key"
   ```

## CI/CD Pipeline

The project includes a GitHub Actions workflow `ci-cd.yml` for:
- **Building Docker Images**: Automatically builds and pushes Docker images to GitHub Container Registry.
- **Deploying Docker Images**: Automatically deploys the application to a production environment by using deployment manifests in helm for K8s cluster.

The project includes a GitHub Actions workflow `helm-manual.yml` for:
- **Deploying Docker Images**: Manually deploys the application to a production environment by using deployment manifests in helm for K8s cluster.

The project includes a GitHub Actions workflow `ansible-manual.yml` for:
- **Running Ansible Playbook**: Manually runs any Ansible playbook defined in the `ansible/playbooks` directory against an EC2 instance securely using SSH and Ansible Vault.

The project includes a GitHub Actions workflow `genai-tests.yml` for:
- **Running GenAI Tests**: Automatically runs the tests defined in the `genai/tests` directory on every code push in genai module.

The project includes a GitHub Actions workflow `server-tests.yml` for:
- **Running Server Tests**: TODO

## API Documentation

### User Service
API documentation is available in the [`server/user/openapi.yml`](server/user/openapi.yml) file.

### Chat Service
API documentation is available in the [`server/chat/openapi.yml`](server/chat/openapi.yml) file.

### GenAI Service
API documentation is available in the [`genai/openapi.yaml`](genai/openapi.yaml) file.


## Requirements

### Functional Requirements
- Recipe Generation: The system must allow users to input a description or ingredients and receive a complete recipe in response.
- Meal Planning: The system must allow users to request a meal plan for a specified number of days and meals per day.
- Chat-Based Interaction: The user must be able to interact with the application via a conversational interface and therefore the system must preserve context in multi-turn conversations.
- Document Upload: The user must be able to upload their own recipe files in a PDF format for analysis or transformation into a structured recipe.
- RAG Integration: The LLM must be able to retrieve information from an internal vector store when generating responses.
- Chat History: The user must be able to view and revisit previous recipe requests and conversations.

### Nonfunctional Requirements
- Performance: The system must generate a recipe in response to a user query within 15 seconds in 95% of cases.
- Scalability: The architecture must allow horizontal scaling of microservices.
- Availability: The system must maintain 99.5% uptime during working hours.
- Reliability: In case of failure in the GenAI service, users must receive a clear error message without crashing the app.
- Security: The system must restrict access to service endpoints from outside world.
- Security: Only authenticated users must be allowed to use the application.
- Modularity: Each microservice must be independently deployable.
- Maintainability: Codebase must follow clean architecture principles and use OpenAPI documentation for APIs.
- Deployability: The system must be deployable via Docker Compose locally and to a Kubernetes cluster via Helm charts.
- Portability: All services must run correctly in both local and cloud environments with minimal configuration.
- Usability: The chat interface must be responsive and intuitive.
- Observability & Monitoring: The system must expose Prometheus metrics for all critical services. Dashboards must be created in Grafana to visualize response latency, error rates, and user request volume. Besides that, at least one alert must be defined.


## Architecture Overview - TODO



## Monitoring and Observability
RecipAI is equipped with a monitoring stack powered by Prometheus and Grafana, deployed in a Kubernetes environment. This setup enables real-time observability across all microservices, including the GenAI service, user and chat services, and the API gateway.

### Prometheus Configuration
Prometheus is configured via a Kubernetes ConfigMap named prometheus-config at [`recipai-chart/templates/prometheus/prometheus-configmap.yml`](recipai-chart/templates/prometheus/prometheus-configmap.yml). The configuration defines scrape jobs for each service, enabling Prometheus to collect metrics every 15 seconds.

All services expose Prometheus-compatible metrics:
- GenAI service uses a standard `/metrics` endpoint.
- Server (Spring Boot) services (e.g., chat, user, api-gw) expose metrics via `/actuator/prometheus`.

For dockerized setup, respective prometheus config is also defined at [`monitoring/prometheus/prometheus.yml`](monitoring/prometheus/prometheus.yml)

### Grafana Dashboards
Grafana is used to visualize metrics collected by Prometheus. It is deployed via Helm and configured with:
- Dashboards for GenAI and chat service latency, error rates, and request counts.
- Dashboard for general system metrics of the cluster
- Contact points for alerting (Discord Webhook)

##### Included Dashboards
- system-dasboard.json: Infrastructure-level metrics (CPU, memory, nodes)
    - For dockerized setup: [`monitoring/grafana/dashboards/system-dashboard.json`](monitoring/grafana/dashboards/system-dashboard.json)
    - For helm setup: [`recipai-chart/dashboards/system-dashboard.json`](recipai-chart/dashboards/system-dashboard.json)
- genai-dashboard.json: latency, error rates, and request counts
    - For dockerized setup: [`monitoring/grafana/dashboards/genai-dashboard.json`](monitoring/grafana/dashboards/genai-dashboard.json)
    - For helm setup: [`recipai-chart/dashboards/genai-dashboard.json`](recipai-chart/dashboards/genai-dashboard.json)
- chat-dashboard.json: latency, error rates, and request counts
    - For dockerized setup: [`monitoring/grafana/dashboards/chat-dashboard.json`](monitoring/grafana/dashboards/chat-dashboard.json)
    - For helm setup: [`recipai-chart/dashboards/chat-dashboard.json`](recipai-chart/dashboards/chat-dashboard.json)

#### Accessing Grafana
- To access Grafana locally from the cluster, we can do port-forwarding:
   ```bash
    kubectl port-forward svc/grafana 3000:3000
   ```
   Then, it should be available at [`http://localhost:3000`](http://localhost:3000)

- Or you can access it directly via our ingress in the cluster: [`https://grafana-tcd.student.k8s.aet.cit.tum.de`](https://grafana-tcd.student.k8s.aet.cit.tum.de)

- Grafana Credentials:
    - Username: `admin`
    - Password: `admin`

### Alerting Setup
Alerts are configured using Grafana’s Unified Alerting system. It defines receivers (e.g., Discord webhook) for high response generation duration in the GenAI service (for >20 seconds).
- Contact points are defined:
    - For dockerized setup: [`monitoring/grafana/provisioning/alerting/contact-points.yaml`](monitoring/grafana/provisioning/alerting/contact-points.yaml)
    - For helm setup: [`recipai-chart/templates/grafana/grafana-alerting.yml`](recipai-chart/templates/grafana/grafana-alerting.yml)

## License

This project is licensed under the MIT License.