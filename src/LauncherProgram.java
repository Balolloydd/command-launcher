import java.util.Scanner;
import java.util.Objects;

import parser.CommandParser;
import parser.ParsedCommand;

import command.CommandDispatcher;

public class LauncherProgram {
    private final CommandParser parser;
    private final CommandDispatcher dispatcher;

    public LauncherProgram(CommandParser parser, CommandDispatcher dispatcher) {
        this.parser = Objects.requireNonNull(parser);
        this.dispatcher = Objects.requireNonNull(dispatcher);
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine().strip();
                if (input.isEmpty()) {
                    System.out.println("Please enter a command.");
                    continue;
                }

                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                try {
                    ParsedCommand parsedCommand = parser.parse(input);
                    dispatcher.dispatch(parsedCommand);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid command: " + e.getMessage());
                } catch (IllegalStateException e) {
                    System.out.println("Execution error: " + e.getMessage());
                }
            }
        }

        System.out.println("Launcher terminated.");
    }
}