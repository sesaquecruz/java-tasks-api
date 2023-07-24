package com.task.api.infrastructure.api;

import com.task.api.infrastructure.E2ETest;
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
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;;
import static org.hamcrest.Matchers.*;

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
}
