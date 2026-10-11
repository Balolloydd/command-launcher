
package app;

import command.CommandDispatcher;
import parser.CommandParser;
import parser.ParsedCommand;

import java.util.Objects;

public class CommandProcessor {
    private final CommandParser parser;
    private final CommandDispatcher dispatcher;

    public CommandProcessor(CommandParser parser, CommandDispatcher dispatcher) {
        this.parser = Objects.requireNonNull(parser);
        this.dispatcher = Objects.requireNonNull(dispatcher);
    }

    public void process(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Command input cannot be empty.");
        }

        ParsedCommand parsedCommand = parser.parse(input);
        dispatcher.dispatch(parsedCommand);
    }
}