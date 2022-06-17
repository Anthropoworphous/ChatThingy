package com.github.anthropoworphous.chatthingy.error.handling;

import com.github.anthropoworphous.chatthingy.log.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ExceptionHandler(Logger logger, @Nullable StackTraceCutter cutter) {
    public ExceptionHandler() {
        this(new Logger(), new StackTraceCutter());
    }

    public void handle(Exception e) {
        if (cutter != null) {
            e.setStackTrace(cutter.cut(e.getStackTrace()));
        }
        logger.log(e);
    }

    public record StackTraceCutter(List<String> foldingHeaders) {
        private static final List<String> defaultFoldingHeaders =
                List.of("java.", "sun.", "discord4j.", "reactor.");

        public StackTraceCutter() {
            this(new ArrayList<>(defaultFoldingHeaders));
        }

        public StackTraceCutter alsoFold(String... headers) { foldingHeaders.addAll(Arrays.asList(headers)); return this; }
        public StackTraceCutter alsoFold(List<String> headers) { foldingHeaders.addAll(headers); return this; }

        public StackTraceElement[] cut(StackTraceElement[] stackTraces) {
            if (stackTraces == null || stackTraces.length == 0) { return stackTraces; }
            List<StackTraceElement> shortened = new ArrayList<>();
            int skipped = 0;
            for (int i = 0; i < stackTraces.length; i++) {
                StackTraceElement trace = stackTraces[i];
                if (defaultFoldingHeaders.stream().anyMatch(trace.getClassName()::startsWith)) {
                    skipped++;
                    continue;
                }
                if (skipped > 0) {
                    StackTraceElement cutStart = stackTraces[i-skipped];
                    shortened.add(new StackTraceElement(
                            cutStart.getClassName().substring(0, 8) + "...",
                            "skipped %d lines from: ".formatted(skipped),
                            "ExceptionHandler-StackTraceCutter",
                            -1));
                    skipped = 0;
                }
                shortened.add(trace);
            }
            if (skipped > 0) {
                StackTraceElement cutStart = stackTraces[stackTraces.length-skipped];
                shortened.add(new StackTraceElement(
                        "Skipped %d lines".formatted(skipped),
                        cutStart.getClassName().substring(0, 8),
                        null, -1 ));
            }
            return shortened.toArray(StackTraceElement[]::new);
        }
    }
}
