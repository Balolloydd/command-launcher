import java.util.Scanner;

import parser.ParsedCommand;
import parser.CommandParser;

import search.SearchCommand;

import command.CommandRegistry;
import command.CommandDispatcher;

public class LauncherProgram {
    public static void run() {
        boolean running = true;
        
        try (Scanner sc = new Scanner(System.in)) {
            CommandParser parser = new CommandParser();

            CommandRegistry registry = new CommandRegistry();
            registry.register(new SearchCommand());

            CommandDispatcher dispatcher = new CommandDispatcher(registry);
            
            while (running) {
                System.out.print("> ");
                String input = sc.nextLine().trim();
                
                if (input.equalsIgnoreCase("exit")) {
                    running = false;
                    System.out.println("Exiting the program.");
                    continue;
                }

                if (input.isEmpty()) {
                    System.out.println("Please enter a command.");
                    continue;
                }

                try {
                    ParsedCommand parsedCommand = parser.parse(input);
                    dispatcher.dispatch(parsedCommand);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}