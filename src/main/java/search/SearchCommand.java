package search;
import command.Command;

public class SearchCommand implements Command {
    private final SearchService searchService;
    
    public SearchCommand(SearchService searchService) {
        this.searchService = searchService;
    }
    
    @Override
    public String name() {
        return "search";
    }

    @Override
    public void execute(String arguments) {
        String query = arguments != null ? arguments.strip() : "";
        if (query.isEmpty()) {
            throw new IllegalArgumentException("Search command requires a search term.");
        }

        searchService.search(query);
    }
}