package com.r3235.tools;

import java.io.Serializable;

/**
 * Класс задания программе.
 * @param <T> Первый аргумент .
 * @param <P> Второй аргумент.
 */
public class Task<T, P> implements Serializable {
    private CommandType type;
    private T firstArgument;
    private P secondArgument;

    public Task(CommandType type) {
        this.type = type;
    }

    public Task(CommandType type, T firstArgument) {
        this.type = type;
        this.firstArgument = firstArgument;
    }

    public Task(CommandType type, T firstArgument, P secondArgument) {
        this.type = type;
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
    }

    public T getFirstArgument() {
        return firstArgument;
    }

    public P getSecondArgument() {
        return secondArgument;
    }

    public CommandType getType() {
        return type;
    }
}
