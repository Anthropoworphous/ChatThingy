package com.github.anthropoworphous.chatthingy.error.handling.throwable;

import java.util.Optional;

@FunctionalInterface
public interface ThrowableFunction<T, R> extends Throwable {
    Result<R> map(T t, Result<R> carrier);

    default Optional<R> map(T t) throws Exception { return Optional.ofNullable(map(t, new Result<>()).get()); }
}
