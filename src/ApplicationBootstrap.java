import browser.BrowserLauncher;
import browser.DefaultBrowserResolver;

import command.CommandRegistry;
import command.CommandDispatcher;

import parser.CommandParser;

import search.SearchCommand;
import search.SearchService;

public final class ApplicationBootstrap {
    private ApplicationBootstrap() {} // Prevents instantiation

    public static LauncherProgram createLauncher() {
        DefaultBrowserResolver resolver = new DefaultBrowserResolver();
        BrowserLauncher browserLauncher = new BrowserLauncher(resolver);

        SearchService searchService = new SearchService(browserLauncher);

        CommandRegistry registry = new CommandRegistry();
        registry.register(new SearchCommand(searchService));
        
        CommandParser parser = new CommandParser();
        CommandDispatcher dispatcher = new CommandDispatcher(registry);

        return new LauncherProgram(parser, dispatcher);
    }
}