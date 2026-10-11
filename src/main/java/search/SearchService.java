package search;

import browser.BrowserLauncher;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SearchService {
    private final BrowserLauncher browserLauncher;

    public SearchService(BrowserLauncher browserLauncher) {
        this.browserLauncher = Objects.requireNonNull(browserLauncher);
    }

    public void search(String query) {
        URI searchUri = buildSearchUri(query);
        browserLauncher.openNewWindow(searchUri);
    }

    private URI buildSearchUri(String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return URI.create("https://www.google.com/search?q=" + encodedQuery);
    }
}