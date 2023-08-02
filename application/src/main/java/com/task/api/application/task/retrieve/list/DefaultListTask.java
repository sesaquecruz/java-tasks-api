package com.task.api.application.task.retrieve.list;

import com.task.api.domain.exceptions.GatewayException;
import com.task.api.domain.pagination.Page;
import com.task.api.domain.task.TaskGateway;
import com.task.api.domain.task.TaskQuery;
import com.task.api.domain.valueobjects.Auth0Identifier;

import static com.task.api.application.utils.IdentifierUtils.buildAuth0Identifier;

public class DefaultListTask extends ListTask {
    public DefaultListTask(TaskGateway taskGateway) {
        super(taskGateway);
    }

    @Override
    public Page<ListTaskOutput> execute(ListTaskInput input) {
        var userId = buildAuth0Identifier(input.userId());
        var query = buildQuery(input);
        return getPage(query, userId);
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

    private Page<ListTaskOutput> getPage(TaskQuery query, Auth0Identifier userId) {
        try {
            return taskGateway
                    .findAll(query, userId)
                    .map(ListTaskOutput::with);
        } catch (Exception ex) {
            throw GatewayException.with(ex);
        }
    }
}
