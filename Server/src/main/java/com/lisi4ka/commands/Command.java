package com.lisi4ka.commands;

public interface Command {
    default String execute(String args) {
        return "\nUnknown command!";
    }
    default String execute() {
        return "\nUnknown command!";
    }
}
