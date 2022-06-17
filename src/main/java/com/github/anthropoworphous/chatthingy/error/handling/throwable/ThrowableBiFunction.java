package com.github.anthropoworphous.chatthingy.error.handling.throwable;

import java.util.Optional;

@FunctionalInterface
public interface ThrowableBiFunction<T1, T2, R> extends Throwable {
    Result<R> map(T1 t1, T2 t2, Result<R> carrier);

    default Optional<R> map(T1 t1, T2 t2) throws Exception {
        return Optional.ofNullable(map(t1, t2, new Result<>()).get());
    }
}
