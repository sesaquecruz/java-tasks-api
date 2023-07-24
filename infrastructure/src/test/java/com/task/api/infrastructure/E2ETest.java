package com.task.api.infrastructure;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@Tag("e2eTest")
@SpringBootTest(classes = TaskApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
public @interface E2ETest {
}
