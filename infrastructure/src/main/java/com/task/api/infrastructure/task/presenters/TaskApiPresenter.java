package com.task.api.infrastructure.task.presenters;

import com.task.api.application.task.retrieve.get.GetTaskOutput;
import com.task.api.application.task.retrieve.list.ListTaskOutput;
import com.task.api.domain.pagination.Page;
import com.task.api.infrastructure.task.models.TaskResponse;

public interface TaskApiPresenter {
    static TaskResponse present(GetTaskOutput output) {
        return new TaskResponse(
                output.id(),
                output.name(),
                output.description(),
                output.priority(),
                output.status(),
                output.dueDate()
        );
    }

    static Page<TaskResponse> present(Page<ListTaskOutput> output) {
        return output
                .map(out -> new TaskResponse(
                        out.id(),
                        out.name(),
                        out.description(),
                        out.priority(),
                        out.status(),
                        out.dueDate()
                ));
    }
}
