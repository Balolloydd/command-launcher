package command;
import parser.ParsedCommand;

public class CommandDispatcher {
      private final CommandRegistry registry;

      public CommandDispatcher(CommandRegistry registry) {
        if (registry == null) {
            throw new IllegalArgumentException("Command Registry cannot be null.");
        }
        
        this.registry = registry;
      }

      public void dispatch(ParsedCommand parsedCommand) {
        if (parsedCommand == null) {
            throw new IllegalArgumentException("Parsed Command cannot be null.");
        }

        Command command = registry.getCommand(parsedCommand.getCommandName());
        if (command == null) {
            throw new IllegalArgumentException("No command registered with name: " + parsedCommand.getCommandName());
        }

        command.execute(parsedCommand.getArguments());
      }
}