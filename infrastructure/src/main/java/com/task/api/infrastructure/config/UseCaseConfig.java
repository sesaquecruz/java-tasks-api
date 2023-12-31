package com.task.api.infrastructure.config;

import com.task.api.application.task.create.CreateTask;
import com.task.api.application.task.create.DefaultCreateTask;
import com.task.api.application.task.delete.DefaultDeleteTask;
import com.task.api.application.task.delete.DeleteTask;
import com.task.api.application.task.retrieve.get.DefaultGetTask;
import com.task.api.application.task.retrieve.get.GetTask;
import com.task.api.application.task.retrieve.list.DefaultListTask;
import com.task.api.application.task.retrieve.list.ListTask;
import com.task.api.application.task.updated.DefaultUpdateTask;
import com.task.api.application.task.updated.UpdateTask;
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

    @Bean
    public ListTask listTask(TaskGateway taskGateway) {
        return new DefaultListTask(taskGateway);
    }

    @Bean
    public UpdateTask updateTask(TaskGateway taskGateway) {
        return new DefaultUpdateTask(taskGateway);
    }

    @Bean
    public DeleteTask deleteTask(TaskGateway taskGateway) {
        return new DefaultDeleteTask(taskGateway);
    }
}
