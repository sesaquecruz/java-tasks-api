package com.task.api.application;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN input);
}
