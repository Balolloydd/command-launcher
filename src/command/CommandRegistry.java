package command;
import java.util.HashMap;

public class CommandRegistry {
    private HashMap<String, Command> commands = new HashMap<>();

    public void register(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null.");
        }
        
        if (commands.containsKey(command.name())) {
            throw new IllegalArgumentException("Command with name '" + command.name() + "' is already registered.");
        }
        
        commands.put(command.name(), command);
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }
}
