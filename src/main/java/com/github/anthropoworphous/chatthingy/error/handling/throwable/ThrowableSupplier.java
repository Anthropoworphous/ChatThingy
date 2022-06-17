package com.github.anthropoworphous.chatthingy.error.handling.throwable;

import java.util.Optional;

@FunctionalInterface
public interface ThrowableSupplier<R> {
    Throwable.Result<R> map(Throwable.Result<R> carrier);

    default Optional<R> map() throws Exception { return Optional.ofNullable(map(new Throwable.Result<>()).get()); }
}
