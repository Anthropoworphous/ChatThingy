package com.github.anthropoworphous.chatthingy.error.handling.throwable;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Throwable {
    class Result<R> {
        private @Nullable R r;
        private @Nullable Exception ex;

        // basic
        public Result<R> carry(R result) {
            r = result;
            ex = null;
            return this;
        }
        public Result<R> carry(Exception error) {
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

        // shortcut
        public R attempt(ResultSupplier<R> resultSupplier) throws Exception {
            return resultSupplier.get();
        }
        public R attempt(ResultSupplier<R> resultSupplier, R fallback) {
            try {
                return resultSupplier.get();
            } catch (Exception e) { return fallback; }
        }
        public @Nullable R suppressedAttempt(ResultSupplier<R> resultSupplier) {
            try {
                return resultSupplier.get();
            } catch (Exception e) { return null; }
        }
        public Optional<R> optionalSuppressedAttempt(ResultSupplier<R> resultSupplier) {
            try {
                return Optional.ofNullable(resultSupplier.get());
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }

    @FunctionalInterface
    interface ResultSupplier<R> {
        R get() throws Exception;
    }
}
