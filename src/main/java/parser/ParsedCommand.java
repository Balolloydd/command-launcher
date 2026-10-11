package parser;

public class ParsedCommand {
    private final String commandName;
    private final String arguments;

    public ParsedCommand(String commandName, String arguments) {
        this.commandName = commandName;
        this.arguments = arguments;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getArguments() {
        return arguments;
    }
}