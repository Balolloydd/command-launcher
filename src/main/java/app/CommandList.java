package app;

import browser.BrowserLauncher;
import browser.DefaultBrowserResolver;

import command.CommandDispatcher;
import command.CommandRegistry;

import parser.CommandParser;

import search.SearchCommand;
import search.SearchService;

public final class CommandList {
    private CommandList() {} // Prevent instantiation

    public static CommandProcessor createProcessor() {
        DefaultBrowserResolver resolver = new DefaultBrowserResolver();

        BrowserLauncher browserLauncher = new BrowserLauncher(resolver);
        SearchService searchService = new SearchService(browserLauncher);
        
        CommandRegistry registry = new CommandRegistry();
        registry.register(new SearchCommand(searchService));
        
        CommandParser parser = new CommandParser();
        CommandDispatcher dispatcher = new CommandDispatcher(registry);

        return new CommandProcessor(parser, dispatcher);
    }
}