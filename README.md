# Task Manager API

This is a task management service that provides secure endpoints for creating, finding, updating, and deleting tasks. It also has features such as finding tasks using pagination, sorting, and searching based on terms.

## Endpoints

| Endpoint              | Method | Protected | Description         |
|-----------------------| ------ |-----------|---------------------|
| `/api/v1/tasks/`      | POST   | YES       | Create a task       |
| `/api/v1/tasks/{id}`  | GET    | YES       | Find a task by id   |
| `/api/v1/tasks`       | GET    | YES       | Search tasks        |
| `/api/v1/tasks`       | PUT    | YES       | Update a task       |
| `/api/v1/tasks`       | DELETE | YES       | Delete a task       |
| `/api/v1/swagger-ui/` | GET    | NO        | API's Documentation |

## Documentation

This service is deployed on Google Cloud Run and its API's documentation can be explored in: [Task API's Doc](https://java-tasks-api-xvtdhw3sfq-uc.a.run.app/api/v1/swagger-ui/index.html)

## Tech Stack

- [Java](https://www.java.com/en/)
- [Spring Boot](https://spring.io/projects/spring-boot/)
- [Spring Security](https://spring.io/projects/spring-security)
- [MySQL](https://www.mysql.com/)

## Frontend Repository

The frontend for this service is implemented using React and can be found in: [Task App Repository](https://github.com/sesaquecruz/react-tasks-app/)

It is deployed on Vercel and can be accessed by visiting: [Task App](https://react-tasks-app-sable.vercel.app/)

## Contributing

Contributions are welcome! If you find a bug or would like to suggest an enhancement, please make a fork, create a new branch with the bugfix or feature, and submit a pull request.

## License

This project is licensed under the MIT License. See [LICENSE](./LICENSE) file for more information.

