package com.github.anthropoworphous.chatthingy.task;

@FunctionalInterface
public interface Task extends Runnable {
    void run();
}
