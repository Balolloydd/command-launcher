import java.util.Scanner;

public class Launcher {
    public static void run() {
        UserInterface ui = new UserInterface();
        
        try (Scanner sc = new Scanner(System.in)) {
            String[] commands = { "open", "search", "exit", "help", "settings", "folder" };
            
            while (true) {
                ui.displayMessage("Enter a command: ");
                String input = sc.nextLine().trim();
                
                boolean isValidCommand = false;
                String[] commandParameters = input.split(" ", 2);

                if (commandParameters.length <= 1) {
                    ui.displayMessage("Please provide a command and its parameters.");
                    continue;
                }
                
                for (String command : commands) {
                    if (commandParameters[0].equals(command)) {
                        isValidCommand = true;
                        break;
                    }
                }
                
                if (isValidCommand) {
                    ui.displayMessage("Executing command: " + input);
                } else {
                    ui.displayMessage("Invalid command. Please try again.");
                    continue;
                }
                
                if (commandParameters[0].equals("exit")) {
                    ui.displayMessage("Exiting the program.");
                    break;
                }
            }
        }
    }


}