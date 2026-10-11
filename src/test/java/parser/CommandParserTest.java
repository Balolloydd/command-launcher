package parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {
    private final CommandParser parser = new CommandParser();

    @Test
    void shouldParseCommandWithArguments() {
        ParsedCommand result = parser.parse("search java arraylist");

        assertEquals("search", result.getCommandName());
        assertEquals("java arraylist", result.getArguments());
    }

    @Test
    void shouldParseCommandWithoutArguments() {
        ParsedCommand result = parser.parse("search");

        assertEquals("search", result.getCommandName());
        assertEquals("", result.getArguments());
    }

    @Test
    void shouldRejectBlankInput() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("   "));
    }
}