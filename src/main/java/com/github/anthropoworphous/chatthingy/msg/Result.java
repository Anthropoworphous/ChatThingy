package com.github.anthropoworphous.chatthingy.msg;

import com.github.anthropoworphous.chatthingy.user.reader.Readers;

import java.util.HashMap;
import java.util.Map;

public class Result {
    public Result(Status status) {
        this.status = status;
    }

    private final Status status;

    public static Map<Readers, Result> getErrors(Map<Readers, Result> results) {
        Map<Readers, Result> result = new HashMap<>();
        results.forEach((reader, r) -> {
            if (r.status != Status.FINE) {
                result.put(reader, r);
            }
        });
        return result;
    }

    public enum Status {
        FINE,
        MUTED,
        DEAFENED,
        MISSING_PERMISSION,
        UNABLE_TO_SEND,
        UNKNOWN_ERROR;
    }
}
