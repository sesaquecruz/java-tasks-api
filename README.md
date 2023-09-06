# Tasks API

This is a Rest API that provides secure endpoints for creating, finding, updating, and deleting tasks. It also allows finding tasks using pagination, sorting, and searching based on terms.

## Endpoints

| Endpoint              | Method | Protected | Description         |
|-----------------------| ------ |-----------|---------------------|
| `/api/v1/tasks/`      | POST   | YES       | Create a task       |
| `/api/v1/tasks/{id}`  | GET    | YES       | Find a task by id   |
| `/api/v1/tasks`       | GET    | YES       | Search tasks        |
| `/api/v1/tasks`       | PUT    | YES       | Update a task       |
| `/api/v1/tasks`       | DELETE | YES       | Delete a task       |
| `/api/v1/swagger-ui/` | GET    | NO        | API's documentation |
| `/api/v1/actuator/`   | GET    | NO        | Health check        |

## Documentation

The API is running in a Kubernetes cluster using GKE on GCP. Check out the documentation on:

- [Tasks API](https://git.tasks.api.sesaque.com/api/v1/swagger-ui/index.html)

Other related repositories are:

- [Tasks App](https://github.com/sesaquecruz/react-tasks-app)
- [Tasks Infra](https://github.com/sesaquecruz/k8s-tasks-infra)
- [Tasks Docker Hub](https://hub.docker.com/r/sesaquecruz/java-tasks-api/tags)


## Tech Stack

- [Java](https://www.java.com/en/)
- [Spring Boot](https://spring.io/projects/spring-boot/)
- [Spring Security](https://spring.io/projects/spring-security)
- [MySQL](https://www.mysql.com/)

## Contributing

Contributions are welcome! If you find a bug or would like to suggest an enhancement, please make a fork, create a new branch with the bugfix or feature, and submit a pull request.

## License

This project is licensed under the MIT License. See [LICENSE](./LICENSE) file for more information.

