package com.github.anthropoworphous.chatthingy.log;

import com.github.anthropoworphous.chatthingy.ChatThingy;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;

public record Logger(@Nullable Writer out) {
    public Logger() {
        this(null);
    }

    public void log(String message) {
        if (out != null) {
            try {
                out.write(message);
            } catch (IOException e) { e.printStackTrace(); } // what else could you do...
        } else if (ChatThingy.plugin() != null) {
            Bukkit.getLogger().info(message);
        } else {
            System.out.println(message);
        }
    }

    public void log(Exception e) {
        if (out != null) {
            try (var pw = new PrintWriter(out)) { e.printStackTrace(pw); }
        } else if (ChatThingy.plugin() != null) {
            Bukkit.getLogger().log(Level.SEVERE, "oops", e);
        } else {
            e.printStackTrace();
        }
    }
}
