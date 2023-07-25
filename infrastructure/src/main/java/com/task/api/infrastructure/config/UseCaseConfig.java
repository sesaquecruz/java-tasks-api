package com.task.api.infrastructure.config;

import com.task.api.application.task.create.CreateTask;
import com.task.api.application.task.create.DefaultCreateTask;
import com.task.api.application.task.retrieve.get.DefaultGetTask;
import com.task.api.application.task.retrieve.get.GetTask;
import com.task.api.domain.task.TaskGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public CreateTask createTask(TaskGateway taskGateway) {
        return new DefaultCreateTask(taskGateway);
    }

    @Bean
    public GetTask getTask(TaskGateway taskGateway) {
        return new DefaultGetTask(taskGateway);
    }
}
