package com.task.api.infrastructure.api;

import com.task.api.domain.task.Task;
import com.task.api.domain.task.valueobjects.Description;
import com.task.api.domain.task.valueobjects.Name;
import com.task.api.domain.task.valueobjects.Priority;
import com.task.api.domain.task.valueobjects.Status;
import com.task.api.domain.valueobjects.Date;
import com.task.api.domain.valueobjects.Identifier;
import com.task.api.infrastructure.E2ETest;
import com.task.api.infrastructure.task.persistence.TaskJpaEntity;
import com.task.api.infrastructure.task.persistence.TaskRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

;

@E2ETest
public class TaskApiTest {
    @LocalServerPort
    private Integer port;
    @Autowired
    private TaskRepository repository;

    @Container
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("tasks")
            .withUsername("root")
            .withPassword("root123");

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL.getMappedPort(3306));
    }

    @BeforeEach
    public void beforeEach() {
        RestAssured.baseURI = "http://localhost:%d/api/v1".formatted(port);
        repository.deleteAll();
    }

    @Test
    public void shouldCreateATaskAndReturnItsLocationWhenDataIsValid() throws JSONException {
        var requestBody = new JSONObject()
                .put("user_id", UUID.randomUUID().toString())
                .put("name", "A name")
                .put("description", "A Description")
                .put("priority", "Normal")
                .put("status", "Pending")
                .put("due_date", Instant.now().truncatedTo(ChronoUnit.MICROS).toString());

        assertThat(repository.count()).isEqualTo(0);

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody.toString()).
        when()
                .post("/tasks").
        then()
                .statusCode(201)
                .assertThat()
                .header("Location", not(emptyString()))
                .extract()
                .response();

        assertThat(repository.count()).isEqualTo(1);

        var taskId = response.header("Location").replace("/tasks/", "");
        var task = repository.findById(taskId).get().toAggregate();

        assertThat(requestBody.getString("user_id")).isEqualTo(task.getUserId().getValue());
        assertThat(requestBody.getString("name")).isEqualTo(task.getName().getValue());
        assertThat(requestBody.getString("description")).isEqualTo(task.getDescription().getValue());
        assertThat(requestBody.getString("priority").toUpperCase()).isEqualTo(task.getPriority().getValue());
        assertThat(requestBody.getString("status").toUpperCase()).isEqualTo(task.getStatus().getValue());
        assertThat(requestBody.getString("due_date")).isEqualTo(task.getDueDate().getValue().toString());
    }

    @Test
    public void shouldReturnCode422AndErrorsWhenDataIsNull() throws JSONException {
        var requestBody = new JSONObject()
                .put("user_id", null)
                .put("name", null)
                .put("description", null)
                .put("priority", null)
                .put("status", null)
                .put("due_date", null);

        var expectedResponseBody = new JSONObject()
                .put("user_id", new JSONArray().put("must not be null"))
                .put("name", new JSONArray().put("must not be null"))
                .put("description", new JSONArray().put("must not be null"))
                .put("priority", new JSONArray().put("must not be null"))
                .put("status", new JSONArray().put("must not be null"))
                .put("due_date", new JSONArray().put("must not be null"));

        assertThat(repository.count()).isEqualTo(0);

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody.toString()).
        when()
                .post("/tasks").
        then()
                .statusCode(422)
                .extract()
                .response();

        assertThat(repository.count()).isEqualTo(0);
        JSONAssert.assertEquals(response.getBody().asString(), expectedResponseBody.toString(), false);
    }

    @Test
    public void shouldReturnCode422AndErrorsWhenDataIsBlank() throws JSONException {
        var requestBody = new JSONObject()
                .put("user_id", " ")
                .put("name", "")
                .put("description", "    ")
                .put("priority", " ")
                .put("status", "")
                .put("due_date", " ");

        var expectedResponseBody = new JSONObject()
                .put("user_id", new JSONArray().put("must not be blank"))
                .put("name", new JSONArray().put("must not be blank"))
                .put("description", new JSONArray().put("must not be blank"))
                .put("priority", new JSONArray().put("must not be blank"))
                .put("status", new JSONArray().put("must not be blank"))
                .put("due_date", new JSONArray().put("must not be blank"));

        assertThat(repository.count()).isEqualTo(0);

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody.toString()).
                when()
                .post("/tasks").
                then()
                .statusCode(422)
                .extract()
                .response();

        assertThat(repository.count()).isEqualTo(0);
        JSONAssert.assertEquals(response.getBody().asString(), expectedResponseBody.toString(), false);
    }

    @Test
    public void shouldReturnATaskWhenIdExists() {
        var task = Task.newTask(
                Identifier.unique(),
                Name.with("A Task"),
                Description.with("A Description"),
                Priority.with("Normal"),
                Status.with("Pending"),
                Date.now()
        );

        assertThat(repository.count()).isEqualTo(0);
        repository.saveAndFlush(TaskJpaEntity.from(task));
        assertThat(repository.count()).isEqualTo(1);

        get("/tasks/%s".formatted(task.getId().getValue())).then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", equalTo(task.getId().getValue()))
                .body("name", equalTo(task.getName().getValue()))
                .body("description", equalTo(task.getDescription().getValue()))
                .body("priority", equalTo(task.getPriority().getValue()))
                .body("status", equalTo(task.getStatus().getValue()))
                .body("due_date", equalTo(task.getDueDate().getValue().toString()));
    }

    @Test
    public void shouldReturnCode404WhenIdDoesNotExist() {
        var id = Identifier.unique().getValue();
        assertThat(repository.count()).isEqualTo(0);

        get("/tasks/%s".formatted(id)).then()
                .statusCode(404)
                .assertThat()
                .body(containsString("task with id %s was not found".formatted(id)));
    }

    @Test
    public void shouldReturnCode400WhenIdIsInvalid() {
        var id = "dfdf1i2891";
        assertThat(repository.count()).isEqualTo(0);

        get("/tasks/%s".formatted(id)).then()
                .statusCode(400)
                .assertThat()
                .body(containsString("invalid id"));
    }

    @Test
    public void shouldReturnTasksOrderedByName() throws JSONException {
        var tasks = saveTasks();
        tasks.sort(Comparator.comparing(t -> t.getName().getValue()));

        var response = given()
                .queryParam("page", 0)
                .queryParam("size", tasks.size()).
        when()
                .get("/tasks").
        then()
                .statusCode(200)
                .extract()
                .response();

        var page = new JSONObject(response.getBody().asString());
        var items = page.getJSONArray("items");

        assertThat(page.getInt("page")).isEqualTo(0);
        assertThat(page.getInt("size")).isEqualTo(tasks.size());
        assertThat(page.getInt("total")).isEqualTo(tasks.size());

        for (int i = 0; i < tasks.size(); i++) {
            var task = tasks.get(i);
            var item = items.getJSONObject(i);

            assertThat(item.getString("name")).isEqualTo(task.getName().getValue());
            assertThat(item.getString("description")).isEqualTo(task.getDescription().getValue());
            assertThat(item.getString("priority")).isEqualTo(task.getPriority().getValue());
            assertThat(item.getString("status")).isEqualTo(task.getStatus().getValue());
            assertThat(item.getString("due_date")).isEqualTo(task.getDueDate().getValue().toString());
        }
    }

    @Test
    public void shouldReturnTasksOrderedByDueDate() throws JSONException {
        var tasks = saveTasks();
        tasks.sort(Comparator.comparing(t -> t.getDueDate().getValue().toString()));

        var response = given()
                .queryParam("page", 0)
                .queryParam("size", tasks.size())
                .queryParam("sort", "dueDate").
        when()
                .get("/tasks").
        then()
                .statusCode(200)
                .extract()
                .response();

        var page = new JSONObject(response.getBody().asString());
        var items = page.getJSONArray("items");

        assertThat(page.getInt("page")).isEqualTo(0);
        assertThat(page.getInt("size")).isEqualTo(tasks.size());
        assertThat(page.getInt("total")).isEqualTo(tasks.size());

        for (int i = 0; i < tasks.size(); i++) {
            var task = tasks.get(i);
            var item = items.getJSONObject(i);

            assertThat(item.getString("name")).isEqualTo(task.getName().getValue());
            assertThat(item.getString("description")).isEqualTo(task.getDescription().getValue());
            assertThat(item.getString("priority")).isEqualTo(task.getPriority().getValue());
            assertThat(item.getString("status")).isEqualTo(task.getStatus().getValue());
            assertThat(item.getString("due_date")).isEqualTo(task.getDueDate().getValue().toString());
        }
    }

    @Test
    public void shouldReturnTaskByPage() throws JSONException {
        var all = saveTasks();
        var tasks = all.stream()
                .sorted(Comparator.comparing(t -> t.getName().getValue()))
                .skip(2)
                .limit(2)
                .toList();

        var response = given()
                .queryParam("page", 1)
                .queryParam("size", 2).
        when()
                .get("/tasks").
        then()
                .statusCode(200)
                .extract()
                .response();

        var page = new JSONObject(response.getBody().asString());
        var items = page.getJSONArray("items");

        assertThat(page.getInt("page")).isEqualTo(1);
        assertThat(page.getInt("size")).isEqualTo(tasks.size());
        assertThat(page.getInt("total")).isEqualTo(all.size());

        for (int i = 0; i < tasks.size(); i++) {
            var task = tasks.get(i);
            var item = items.getJSONObject(i);

            assertThat(item.getString("name")).isEqualTo(task.getName().getValue());
            assertThat(item.getString("description")).isEqualTo(task.getDescription().getValue());
            assertThat(item.getString("priority")).isEqualTo(task.getPriority().getValue());
            assertThat(item.getString("status")).isEqualTo(task.getStatus().getValue());
            assertThat(item.getString("due_date")).isEqualTo(task.getDueDate().getValue().toString());
        }
    }

    @Test
    public void shouldReturnTasksByTerm() throws JSONException {
        var term = "NORMAL";

        var all = saveTasks();
        var tasks = all.stream()
                .sorted(Comparator.comparing(t -> t.getName().getValue()))
                .filter(t -> t.getName().getValue().contains(term) ||
                        t.getDescription().getValue().contains(term) ||
                        t.getPriority().getValue().contains(term) ||
                        t.getStatus().getValue().contains(term)
                )
                .toList();

        var response = given()
                .queryParam("term", term).
        when()
                .get("/tasks").
        then()
                .statusCode(200)
                .extract()
                .response();

        var page = new JSONObject(response.getBody().asString());
        var items = page.getJSONArray("items");

        assertThat(page.getInt("page")).isEqualTo(0);
        assertThat(page.getInt("size")).isEqualTo(tasks.size());
        assertThat(page.getInt("total")).isEqualTo(tasks.size());

        for (int i = 0; i < tasks.size(); i++) {
            var task = tasks.get(i);
            var item = items.getJSONObject(i);

            assertThat(item.getString("name")).isEqualTo(task.getName().getValue());
            assertThat(item.getString("description")).isEqualTo(task.getDescription().getValue());
            assertThat(item.getString("priority")).isEqualTo(task.getPriority().getValue());
            assertThat(item.getString("status")).isEqualTo(task.getStatus().getValue());
            assertThat(item.getString("due_date")).isEqualTo(task.getDueDate().getValue().toString());
        }
    }

    private List<Task> saveTasks() {
        var tasks = new ArrayList<>(List.of(
                Task.newTask(
                        Identifier.unique(),
                        Name.with("One"),
                        Description.with("NORMAL"),
                        Priority.with("LOW"),
                        Status.with("CANCELLED"),
                        Date.now()
                ),
                Task.newTask(
                        Identifier.unique(),
                        Name.with("Two"),
                        Description.with("Bike"),
                        Priority.with("NORMAL"),
                        Status.with("PENDING"),
                        Date.now()
                ),
                Task.newTask(
                        Identifier.unique(),
                        Name.with("Three"),
                        Description.with("Bike"),
                        Priority.with("NORMAL"),
                        Status.with("COMPLETED"),
                        Date.now()
                ),
                Task.newTask(
                        Identifier.unique(),
                        Name.with("Four"),
                        Description.with("Surf"),
                        Priority.with("HIGH"),
                        Status.with("COMPLETED"),
                        Date.now()
                ),
                Task.newTask(
                        Identifier.unique(),
                        Name.with("Five"),
                        Description.with("Hike"),
                        Priority.with("HIGH"),
                        Status.with("PENDING"),
                        Date.now()
                )
        ));

        repository.saveAllAndFlush(tasks.stream().map(TaskJpaEntity::from).toList());
        return tasks;
    }
}
