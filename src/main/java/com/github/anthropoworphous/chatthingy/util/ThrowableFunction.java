package com.github.anthropoworphous.chatthingy.util;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@FunctionalInterface
public interface ThrowableFunction<T, R> {
    PossibleErrorOrResult<R> map(T t, PossibleErrorOrResult<R> errorCarrier);

    default Optional<R> map(T t) throws Exception {
        return Optional.ofNullable(map(t, new ThrowableFunction.PossibleErrorOrResult<>()).get());
    }



    class PossibleErrorOrResult<R> {
        private @Nullable R r;
        private @Nullable Exception ex;

        public PossibleErrorOrResult<R> carry(R result) {
            r = result;
            ex = null;
            return this;
        }

        public PossibleErrorOrResult<R> carry(Exception error) {
            r = null;
            ex = error;
            return this;
        }

        public @Nullable R get() throws Exception {
            if (ex != null) {
                throw ex;
            }
            return r;
        }
    }
}
