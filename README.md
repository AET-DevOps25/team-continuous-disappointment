# RecipAI

RecipAI is available at [`https://recipai.student.k8s.aet.cit.tum.de`](https://recipai.student.k8s.aet.cit.tum.de) (k8s) and deployable at [`https://recipai.duckdns.org`](https://recipai.duckdns.org) (aws)

For running it on docker please see [dockerized-deployment](#dockerized-deployment) and [.env.template](.env.template).

## Project Description

### Application Overview

#### Main Page
![RecipAI](docs/images/recipai_main.png)

#### User Preferences
![RecipAI Preferences](docs/images/recipai_preferences.png)

### Main Functionality

The primary functionality of the application is to enable users to generate and explore recipes through a natural language interface powered by a LLM. The application allows users to search for recipes based on ingredients, generate meal plans, and receive step-by-step cooking instructions tailored to their preferences and dietary requirements.

### Intended Users

The application is designed for home cooks, culinary enthusiasts, individuals with dietary restrictions, and anyone interested in exploring or generating recipes in an intuitive and flexible manner. It is particularly useful for users who wish to interact with a recipe database using natural language queries.

### Integration of Generative AI

Generative AI is integrated meaningfully through a dedicated LLM microservice developed in Python. This service processes user inputs in natural language, generates recipes based on the provided ingredients, modifies existing recipes according to user needs, and provides meal suggestions based on the user dietary preferences. The use of GenAI enhances the user experience by offering a personalized RAG system that emphasizes user-specific recipe collections. Through the use of a conversational AI system, we provide to the users with a multi-turn chat with context preservation.

### Usage Guide

RecipAI provides an intuitive chat-based interface for generating recipes, meal plans, and managing dietary preferences. Here's how to use the application:

#### Getting Started
1. **Login**: Access the application at the deployed URL and sign in using your GitLab LRZ credentials
2. **Set Preferences**: Configure your dietary restrictions and preferences (vegan, gluten-free, diabetic, etc.) in the user settings
3. **Start Chatting**: Use the main chat interface to interact with the AI recipe assistant

#### Tips for Best Results
- Be specific about dietary restrictions, cooking time, or cuisine preferences
- Include the number of servings when requesting recipes
- Mention available cooking equipment or preferred cooking methods
- Ask follow-up questions to refine or modify generated recipes


### Functional Scenarios

1. **Ingredient-Based Recipe Generation**: A user inputs, "Suggest a quick dinner recipe with chicken and broccoli." The system uses the LLM to generate a relevant recipe, which is presented to the user through the user interface.

2. **Recipe Modification**: A user submits a traditional recipe and requests, "Make this vegan." The LLM identifies non-vegan ingredients and substitutes them with plant-based alternatives, returning a modified version of the recipe.

3. **Meal Planning with User Preferences**: A user defines his/her dietary preferences in the web application and asks for a weekly meal plan in the chat. The LLM generates a diverse and nutritionally balanced plan, based on dietary restrictions or cuisine preferences.

4. **Ingredient-Limited Cooking**: A user specifies available ingredients, such as "eggs, spinach, and cheese," and the system suggests recipes that can be prepared using those ingredients, optimizing for simplicity and flavor.

## Components and Responsibilities (System Design)

### 1. Frontend (React)

**_Responsible Students:_**

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
- Recipe document upload
- Recipe history viewer

### 2. Backend (Spring Boot REST API)

**_Responsible Student:_**

- Mehmed Esad Akcam

**Responsibilities:**

- Authenticate and authorize users via GitLab LRZ.
- Manage user profiles and dietary preferences.
- Route prompts to the GenAI service and handle responses.
- Store and retrieve chat and recipe history from MongoDB.
- Expose REST endpoints for client-side operations.

### 3. GenAI Microservice (Python with FastAPI + LangChain + Qdrant)

**_Responsible Student:_**

- Ege Dogu Kaya

**Responsibilities:**

- Process incoming prompts and preferences.
- Use LLM (e.g., GPT via LangChain or Llama3) to generate, modify, and plan recipes.
- Fetch additional data from a well known recipe source stored in the vector database Qdrant to enable document retrieval for answering recipe document based questions.
- Structure outputs into JSON responses and provide endpoints via FastAPI for server module.

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

- `recipes` – Stores user specific embedded recipe documents which are uploaded by the user

### 6. DevOps

**_Responsible Students:_**

- Mehmed Esad Akcam & Ege Dogu Kaya

**_Responsibilites_**

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
2. User select his/her didtary preferences.
2. User types: _"Suggest a dinner with lentils."_
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

## Project Requirements Checklist

We fulfilled all the project requirements for RecipAI. You can find our project requirements checklist here: [Project Requirements Checklist](requirements.md)

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

**Note**: Please be aware that you need to manually add the GITLAB_CLIENT_SECRET from the `.env` file (see [.env.template](.env.template)) to the [application.yaml](server/api-gw/src/main/resources/application.yaml) file for the field `client-secret` for local development.
```bash
spring:
   profiles:
      active: dev
   application:
      name: api-gw
   cloud:
      gateway:
         mvc:
         routes:
            - id: user
               uri: http://user-service:8081
               predicates:
               - Path=/user/**
            - id: chat
               uri: http://chat-service:8082
               predicates:
               - Path=/chat/**
   security:
      oauth2:
         resourceserver:
         opaquetoken:
            client-id: ${GITLAB_CLIENT_ID:60a9e442420a386f2ddff0f60ed0801dd7e826f0710507e982d5afe6aa054334}
            client-secret: -> PUT HERE <-
            introspection-uri: https://gitlab.lrz.de/oauth/introspect


   server:
   port: 8080

   management:
   endpoints:
      web:
         exposure:
         include:
            - health
            - info
            - metrics
            - prometheus
   ```

1. Navigate to the `server` directory:
   ```bash
   cd server
   ```
2. Build the project:
   ```bash
   ./gradlew build
   ```

### GenAI Service Setup

1. Navigate to the `genai` directory:
   ```bash
   cd genai
   ```
2. Install dependencies:
   ```bash
   python3 -m venv .venv
   source .venv/bin/activate
   pip3 install -r requirements.txt
   ```

## Test Instructions

### Running Tests in Server

1. Navigate to the `server` directory:
   ```bash
   cd server
   ```
2. Test the microservice - API Gateway:
   ```bash
   ./gradlew :api-gw:test
   ```
3. Test the microservice - User:
   ```bash
   ./gradlew :user:test
   ```
4. Test the microservice - Chat:
   ```bash
   ./gradlew :chat:test
   ```
### Running Tests in GenAI
1. Navigate to the `genai` directory:
   ```bash
   cd genai
   ```
2. Test the microservice - GenAI:
   ```bash
   pytest
   ```
Sidenote: In order to pass all genai tests, you need to have a Qdrant instance running on your local machine,
because one of the integration tests requires a running Qdrant instance.

### Running Tests in Client
1. Navigate to the `client` directory:
   ```bash
   cd client
   ```
2. Test the microservice - Client:
   ```bash
   npm run test -- --run
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
- Stores embedded documents in a vector database to be able to make similarity search and document retrieval.
- Source code is in the `genai` directory.
- Tests are in the `genai/tests` directory.

#### GenAI Usage

- The GenAI service is responsible for all interactions with the language model used to generate and modify recipes in RecipAI. It receives user inputs as free-text prompts and responds with structured outputs such as complete recipes, meal plans, or modified instructions. It is implemented using FastAPI, LangChain, and integrates with local and cloud large language models.

##### Retrieval-Augmented Generation

- The GenAI service uses Qdrant as a vector store to retrieve relevant documents before querying the LLM. It adds the retrieved context to the prompt to improve the relevance of answers.

##### Integration

- The client UI sends user requests to the API gateway, which forwards them to the chat service. Chat service forwards them to the GenAI service along with the user’s query and chat history to support multi-turn conversations. GenAI service then makes a similarity search in the vector database with the given query, and generates a respective answer. GenAI service is able to provide a proper answer altough no similar context is found in the vector database. (Endpoint: POST - `genai/generate`)
- If the user wants to upload a recipe file, client UI sends the file content directly to the API gateway, which forwards to the GenAI service, where the content of the file is chunked, embedded, and stored in the vector database. (Endpoint: POST - `genai/upload`)
- For using/testing the upload functionality, you can find some recipe PDFs to test the upload under `recipe_pdfs` folder. If you want, you can also modify the content of the script to generate your own recipe PDFs which can be found under `recipe_pdfs/scripts` folder. You can run the script from the root folder like:
   - ```bash
      python recipe_pdfs/scripts/basic_recipes.py
      ```

##### Vector Database - Qdrant

We use Qdrant as the vector database to enable semantic search and retrieval-augmented generation (RAG) in RecipAI. Embeddings are generated using OpenAI’s small embedding model `text-embedding-3-small`.

```bash
# Example: Creating OpenAI embeddings for ingestion
embeddings = OpenAIEmbeddings(
    model="text-embedding-3-small",
    openai_api_key=Config.api_key_openai
)
```

##### Environment Variables for GenAI

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
- However, you do not need to set all of these fields in your `.env` file. To run the GenAI module, you need at least the API_OPENAI, API_OPENWEBUI, and BASE_URL variables. You can find more information in the [.env.template](.env.template) file.

- Example for Cloud LLM Models (defined in `genai/service/llm_service.py`):

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

- Example for Local LLM Models (defined in `genai/service/llm_service.py`):

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
   docker compose -f docker-compose.yml up -d
   ```
2. Access the application:
   - Client: [http://localhost:5173](http://localhost:5173)
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

The project includes Helm charts for Kubernetes deployment in the `infra/recipai-chart` directory.

### Deploy with Helm

1. Install the Helm chart:
   ```bash
   helm -n <your namespace> install recip-ai ./infra/recipai-chart \
            --set secrets.gitlabClientSecret="your gitlab client secret" \
            --set secrets.mongodbAdminPassword="your mongodb admin password" \
            --set secrets.apiOpenAi="your open ai api key" \
            --set secrets.apiOpenWebUi="your open web ui api key"
            --set secrets.discordWebhookUrl="${{ your discord webhook url }}"
   ```

## AWS Deployment

### Deploy with Ansible

1. Change the host name of the server on [inventory.ini](./infra/ansible/inventory.ini) and [ansible-manual](./.github/workflows/ansible-manual.yml) to the server or ip address of VM.
2. Update `recipai.duckdns.org` DNS record on DuckDNS to point new VM IP address
3. The application is now deployable through github actions with the following steps
   - Run Manual Ansible Playbook Execution action install_docker.yml as input
   - Run Manual Ansible Playbook Execution action with docker_compose_up.yml
   - Run Manual Ansible Playbook Execution action with install_caddy.yml

## CI/CD Pipeline

The project includes a GitHub Actions workflow `ci-cd.yml` for:
- **Building Docker Images**: Automatically builds and pushes Docker images to GitHub Container Registry.
- **Deploying Docker Images**: Automatically deploys the application to a production environment by using deployment manifests in helm for K8s cluster.
- **Running Server Tests**: For each push, server tests on server microservices are run.
- **Running GenAI Tests**: Automatically runs the tests defined in the `genai/tests` directory on every code push in genai module.
- **Running Client Tests**: Automatically runs the UI tests defined in the `client/src/components/__tests__` directory on every code push in client module.

### CI/CD Pipeline Workflow (`ci-cd.yml`)

**Triggers:**
- Push to `main` or `feature/**` branches
- Changes in `genai/**`, `server/**`, `client/**`, or workflow files

**Features:**
- **Change Detection**: Uses `dorny/paths-filter` to detect which services have changed
- **Conditional Builds**: Only builds and tests services that have been modified
- **Multi-stage Pipeline**: Build → Test → Docker → Deploy

**Jobs:**
- `detect-changes`: Identifies which services need building/testing
- `build-genai`: Python linting, dependency installation, and genai tests
- `build-server`: Java microservices build and test (API Gateway, Chat, User)
- `build-client`: Node.js build and test
- `docker-release-*`: Builds and pushes Docker images to GitHub Container Registry
- `helm-deploy`: Deploys to Kubernetes using Helm charts

The project includes a GitHub Actions workflow `helm-manual.yml` for:

- **Deploying Docker Images**: Manually deploys the application to a production environment by using deployment manifests in helm for K8s cluster.

The project includes a GitHub Actions workflow `ansible-manual.yml` for:

- **Running Ansible Playbook**: Manually runs any Ansible playbook defined in the `infra/ansible/playbooks` directory against an EC2 instance securely using SSH and Ansible Vault.

## API Documentation

REST API documentation for User, Chat, API Gateway, and GenaAI is available in the [`api/openapi.yaml`](api/openapi.yaml) file.

We also publish our REST API documentation via Github Pages automatically:

📄 Swagger UI: [https://aet-devops25.github.io/team-continuous-disappointment/api-documentation/](https://aet-devops25.github.io/team-continuous-disappointment/api-documentation/)

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

## Architecture Overview

### UML Component Diagram

The following UML component diagram shows the details of the RecipAI application architecture and provides a comprehensive overview of the interfaces offered by the genai, chat, user, and API gateway services.
![Component Diagram](docs/architecture_diagrams/component_diagram.png)

### UML Class Diagram - Server
The following UML class diagram shows the details of the RecipAI application server’s repository layer, service layer, and controller layer.
![Server Class Diagram](docs/architecture_diagrams/server_class_diagram.png)

### UML Class Diagram - GenAI
The following UML class diagram shows the details of the RecipAI GenAI module's repository layer, service layer, and controller layer.
![GenAI Class Diagram](docs/architecture_diagrams/genai_class_diagram.png)

### UML Use Case Diagram
The following UML use case diagram shows the use cases and the participating actors of the RecipAI web application.
![Use Case Diagram](docs/architecture_diagrams/use_case_diagram.png)

## Monitoring and Observability

RecipAI is equipped with a monitoring stack powered by Prometheus and Grafana, deployed in a Kubernetes environment. This setup enables real-time observability across all microservices, including the GenAI service, user and chat services, and the API gateway.

### Prometheus Configuration

Prometheus is configured via a Kubernetes ConfigMap named prometheus-config at [`infra/recipai-chart/templates/prometheus/prometheus-configmap.yml`](infra/recipai-chart/templates/prometheus/prometheus-configmap.yml). The configuration defines scrape jobs for each service, enabling Prometheus to collect metrics every 15 seconds.

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
  - For helm setup: [`infra/recipai-chart/dashboards/system-dashboard.json`](infra/recipai-chart/dashboards/system-dashboard.json)
  - ![System Dashboard](docs/images/system-dashboard.png)
- genai-dashboard.json: latency, error rates, and request counts
  - For dockerized setup: [`monitoring/grafana/dashboards/genai-dashboard.json`](monitoring/grafana/dashboards/genai-dashboard.json)
  - For helm setup: [`infra/recipai-chart/dashboards/genai-dashboard.json`](infra/recipai-chart/dashboards/genai-dashboard.json)
  - ![GenAI Dashboard](docs/images/genai-dashboard.png)
- chat-dashboard.json: latency, error rates, and request counts
  - For dockerized setup: [`monitoring/grafana/dashboards/chat-dashboard.json`](monitoring/grafana/dashboards/chat-dashboard.json)
  - For helm setup: [`infra/recipai-chart/dashboards/chat-dashboard.json`](infra/recipai-chart/dashboards/chat-dashboard.json)
  - ![Chat Dashboard](docs/images/chat-dashboard.png)

### Grafana Logs

RecipAI collects all service logs via Loki and Promtail for centralized logging. Promtail is configured to scrape logs from all services running in the cluster and forwards them to Loki for storage. This enables log aggregation and visualization through Grafana's built-in log viewer.

Example - Logs from the GenAI Service:

![Grafana Logging](docs/images/grafana_logging.png)


### Accessing Grafana

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

Alerts are configured using Grafana’s Unified Alerting system. It defines our Discord webhook as the receiver for high response generation duration in the GenAI service (for >15 seconds).

- Contact points are defined:
  - For dockerized setup: [`monitoring/grafana/provisioning/alerting/contact-points.yaml`](monitoring/grafana/provisioning/alerting/contact-points.yaml)
  - For helm setup: [`infra/recipai-chart/templates/grafana/grafana-alerting.yml`](infra/recipai-chart/templates/grafana/grafana-alerting.yml)

- Discord webhook as our contact point:
   - ![Discord Webhook Configuration](docs/images/alerts.png)

## License

This project is licensed under the MIT License.
