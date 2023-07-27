package com.task.api.application.task.retrieve.list;

import com.task.api.domain.exceptions.GatewayException;
import com.task.api.domain.pagination.Page;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.task.TaskQuery;

public class DefaultListTask extends ListTask {
    public DefaultListTask(TaskGateway taskGateway) {
        super(taskGateway);
    }

    @Override
    public Page<ListTaskOutput> execute(ListTaskInput input) {
        return getPage(buildQuery(input));
    }

    private TaskQuery buildQuery(ListTaskInput input) {
        return TaskQuery.with(
                input.page(),
                input.size(),
                input.term(),
                input.sort(),
                input.direction()
         );
    }

    private Page<ListTaskOutput> getPage(TaskQuery query) {
        try {
            return taskGateway
                    .findAll(query)
                    .map(ListTaskOutput::with);
        } catch (Exception ex) {
            throw GatewayException.with(ex);
        }
    }
}
