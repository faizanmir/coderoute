# CodeRoute

CodeRoute is an experimental platform for automated code review. It is organised as a set of Kotlin/Spring Boot microservices that can analyse pull requests, generate AI powered feedback and publish comments back to the hosting provider.

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

---

## Project structure

The repository is a Maven multi module build. Each service is responsible for a single part of the review workflow:

* **webhook-handler-service** – receives webhook events to trigger a review.
* **pr-analyzer-service** – downloads pull request data and prepares the code diff.
* **ai-reviewer-service** – calls the AI model to produce review suggestions.
* **comment-publisher-service** – posts review comments back to the pull request.
* **comment-persistence-service** – stores review results for auditing.
* **core** – shared domain objects and utilities.
* **core-config** – common Spring configuration used across modules.

At the time of writing the services are mostly scaffolding, but they illustrate the intended architecture.

---

## Getting started

### Prerequisites

* JDK 21+
* Maven or the provided Maven Wrapper (`./mvnw`)

### Build

```bash
./mvnw clean install
```

### Run a service

Each service can be started independently, for example:

```bash
./mvnw -pl webhook-handler-service spring-boot:run
```

---

## Contributing

Contributions are welcome. Feel free to open an issue or submit a pull request with improvements.

---

## License

Distributed under the MIT License. See [LICENSE](LICENSE) for more information.

---

## Contact

Faizan Mir – [@faizanmir](https://twitter.com/faizanmir) – faizanmir.dev@gmail.com

Project Link: <https://github.com/faizanmir/coderoute>

