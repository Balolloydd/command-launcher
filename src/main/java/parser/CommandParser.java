package parser;

public class CommandParser {
    
    public ParsedCommand parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input command cannot be null or empty.");
        }
        
        String[] parts = input.trim().split("\\s+", 2);
        
        String commandName = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";

        return new ParsedCommand(commandName, arguments);
    }


}
