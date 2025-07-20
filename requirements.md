# Project Requirements Checklist
## 1. 🧩 Functional Application (20 Points)

| Criteria  |	Points | Status | Where to find |
|--------|--------|--------|--------|
| End-to-end functionality between all components (client, server, database) | 	6 | ✅ | Application works fine at [`https://recipai.student.k8s.aet.cit.tum.de`](https://recipai.student.k8s.aet.cit.tum.de)|
| Smooth and usable user interface|  	4 | ✅ | e.g. [Main Page](README.md#main-page), [User Preferences](README.md#user-preferences) |
| REST API is clearly defined and matches functional needs|  	4 | ✅ | [openapi.yaml](api/openapi.yaml) |
| Server Side has at least 3 microservices |  	4 |  ✅ | [API-Gateway](server/api-gw/), [User](server/user/), [Chat](server/user/)|
| Application topic is appropriately chosen and fits project objectives | 	2 |  ✅ |  [Project Description](README.md#project-description), [Functional and Nonfunctional Requirements](README.md#requirements) |

## 2. 🤖 GenAI Integration (10 Points)

| Criteria  |	Points | Status | Where to find |
|--------|--------|--------|--------|
| GenAI module is well-embedded and fulfills a real user-facing purpose | 	4 | ✅ | [GenAI Integration](README.md#integration-of-generative-ai), [Techical Details of GenAI](README.md#genai-service-development) |
| Connects to cloud/local LLM | 4 | ✅ | [Cloud and Local LLM Integration](genai/service/llm_service.py), [Cloud LLM](genai/rag/llm/cloud_chat_model.py), [Local LLM](genai/rag/llm/chat_model.py)  |
| Modularity of the GenAI logic as a microservice | 2 | ✅ | [Dockerfile](genai/Dockerfile) |

## 3. 🐳 Containerization & Local Setup (10 Points)

| Criteria  |	Points | Status | Where to find |
|--------|--------|--------|--------|
| Each component is containerized and runnable in isolation | 6 | ✅ | [GenAI Docker Image](https://github.com/AET-DevOps25/team-continuous-disappointment/pkgs/container/team-continuous-disappointment%2Fgenai), [API Gateway Docker Image](https://github.com/AET-DevOps25/team-continuous-disappointment/pkgs/container/team-continuous-disappointment%2Fapi-gw), [Chat Docker Image](https://github.com/AET-DevOps25/team-continuous-disappointment/pkgs/container/team-continuous-disappointment%2Fchat), [User Docker Image](https://github.com/AET-DevOps25/team-continuous-disappointment/pkgs/container/team-continuous-disappointment%2Fuser), [Client Docker Image](https://github.com/AET-DevOps25/team-continuous-disappointment/pkgs/container/team-continuous-disappointment%2Fclient)|
| docker-compose.yml enables local development and testing with minimal effort and provides sane defaults (no complex env setup required) | 4 | ✅ | [Docker Compose File](docker-compose.yml), For Local Development: [Docker Compose Dev File](docker-compose-dev.yml) |

## 4. 🔁 CI/CD & Deployment (20 Points)

| Criteria  |	Points | Status | Where to find |
|--------|--------|--------|--------|
| CI pipeline with build, test, and Docker image generation via GitHub Actions | 8 | ✅ | [CI/CD Pipeline](https://github.com/AET-DevOps25/team-continuous-disappointment/actions/workflows/ci-cd.yml), [CI/CD Github Actions File](.github/workflows/ci-cd.yml)|
| CD pipeline set up to automatically deploy to Kubernetes on main merge | 6 | ✅ | Line 256: helm-deploy in [CI/CD Github Actions File](.github/workflows/ci-cd.yml)  |
| Deployment works on our infrastructure (Rancher) and Cloud (AWS) | 6 | ✅ | [Rancher Deployment](.github/workflows/ci-cd.yml), [AWS Deployment with Ansible](.github/workflows/ansible-manual.yml) and [Ansible Playbooks](infra/ansible/playbooks/) | 

## 5. 📊 Monitoring & Observability (10 Points)

| Criteria  |	Points | Status | Where to find |
|--------|--------|--------|--------|
| Prometheus integrated and collecting meaningful metrics | 4 | ✅ | [Kubernetes](infra/recipai-chart/templates/prometheus/), [Docker](monitoring/prometheus/)|
| Grafana dashboards for system behavior visualization | 4 | ✅ | Kubernetes: [GenAI Dashboard](infra/recipai-chart/dashboards/genai-dashboard.json), [Chat Dasboard](infra/recipai-chart/dashboards/chat-dashboard.json), [System Dashboard](infra/recipai-chart/dashboards/system-dashboard.json); Docker: [GenAI Dashboard](monitoring/grafana/provisioning/dashboards/genai-dashboard.json), [Chat Dasboard](monitoring/grafana/provisioning/dashboards/chat-dashboard.json), [System Dashboard](monitoring/grafana/provisioning/dashboards/system-dashboard.json) |
| At least one alert rule set up | 2 | ✅ | [Discord Alert in Kubernetes](infra/recipai-chart/templates/grafana/grafana-alerting.yml), [Discord Alert in Docker](monitoring/grafana/provisioning/alerting/) |

## 6. 🧪 Testing & Structured Engineering Process (20 Points)

| Criteria  |	Points | Status | Where to find |
|--------|--------|--------|--------|
| Test cases implemented for server/client and GenAI logic | 6 | ✅ | [Server: Chat](server/chat/src/test/java/com/continiousdisappointment/chat/ChatServiceTest.java), [Server: User](server/user/src/test/java/com/continiousdisappointment/user/UserServiceTest.java), [GenAI](genai/tests/), [Client](client/src/components/__tests__/)|
| Evidence of software engineering process: documented requirements, architecture models, such as top-level architecture, use case diagramm and analysis object model. | 10 | ✅ | [UML Diagrams](docs/architecture_diagrams/), [Functional and Nonfunctional Requirements](README.md#requirements) |
| Tests run automatically in CI and cover key functionality | 4 | ✅ | [All tests run automatically in CI/CD](.github/workflows/ci-cd.yml) |

## 7. 📚 Documentation & Weekly Reporting (10 Points)

| Criteria  |	Points | Status | Where to find |
|--------|--------|--------|--------|
| README.md and some parts in Confluence includes setup instructions, architecture overview, usage guide, and a clear mapping of student responsibilities | 2 | ✅ | [Setup Instructions](README.md#setup-instructions), [Architecture Overview](README.md#architecture-overview), [Usage Guide](README.md#usage-guide), [Student Responsibilities](README.md#components-and-responsibilities-system-design), [Weekly Reports](https://confluence.aet.cit.tum.de/spaces/DO25WR/pages/258580182/team+continous+disappointment)|
| Documentation of CI/CD setup, and GenAI usage included | 2 | ✅ | [CI/CD Setup](README.md#cicd-pipeline), [GenAI Usage](README.md#genai-usage) |
| Deployment and local setup instructions are clear, reproducible, and platform-specific (≤3 commands for local setup, with sane defaults) | 2 | ✅ | [Local Setup Instructions](README.md#setup-instructions), [Running the Application](README.md#running-the-application), [Deployment Instructions](README.md#cicd-pipeline) |
| Subsystems have documented interfaces (API-driven deployment, e.g. Swagger/OpenAPI) | 2 | ✅ | [openapi.yaml](api/openapi.yaml), API Documentation published via Github Pages [`https://aet-devops25.github.io/team-continuous-disappointment/api-documentation/`](https://aet-devops25.github.io/team-continuous-disappointment/api-documentation/) |
| Monitoring instructions included in the documentation and exported as files | 2 | ✅ | [Monitoring and Observability](README.md#monitoring-and-observability) |

## 🏅 Bonus Points (up to +5)

| Criteria  |	Points | Status | Where to find |
|--------|--------|--------|--------|
| Advanced Kubernetes use (e.g., self-healing, custom operators, auto-scaling) | +1 | ❌ | - |
| Full RAG pipeline implementation (with vector DB like Weaviate) | +1 | ✅ | [Qdrant Vector Database](genai/vector_database/qdrant_vdb.py), [Qdrant Service](genai/service/qdrant_service.py), [Ingestion Pipeline](genai/service/ingestion_service.py), [RAG Service](genai/service/rag_service.py) |
| Real-world-grade observability (e.g., log aggregation, tracing) | +1 | ✅ | Log Aggregation implemented: [Loki for Kubernetes](infra/recipai-chart/templates/loki/), [Promtail for Kubernetes](infra/recipai-chart/templates/promtail/), [Loki for Docker](monitoring/loki/), [Promtail for Docker](monitoring/promtail/) |
| Beautiful, original UI or impactful project topic | +1 | ❓ | We think RecipAI has a beautiful UI and a nice project for cooking enthusiasts and individuals with dietary restrictions 👩‍🍳👨‍🍳 |
| Advanced monitoring setup with extensive and meaningful metrics (e.g., custom Prometheus exporters or Grafana dashboards with annotated insights) | +1 | ✅ | [Extensive Prometheus Metrics for GenAI with Grafana Dashboard](infra/recipai-chart/dashboards/genai-dashboard.json), [Extensive Prometheus Metrics for Chat Service with Grafana Dashboard](infra/recipai-chart/dashboards/chat-dashboard.json), [Custom Node-level CPU/memory monitoring with Grafana Dashboard](infra/recipai-chart/dashboards/system-dashboard.json) |