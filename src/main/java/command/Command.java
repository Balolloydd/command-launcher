package command;

public interface Command {
    String name();
    void execute(String arguments);
}
