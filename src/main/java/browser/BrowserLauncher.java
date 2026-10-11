package browser;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class BrowserLauncher {

    private final DefaultBrowserResolver resolver;

    public BrowserLauncher(DefaultBrowserResolver resolver) {
        this.resolver = Objects.requireNonNull(resolver);
    }

    public void openNewWindow(URI uri) {
        Objects.requireNonNull(uri);
        Path browserPath = resolver.resolve();
        
        if (!Files.isRegularFile(browserPath)) {
            throw new IllegalStateException("Browser executable not found: " + browserPath);
        }

        try {
            new ProcessBuilder(browserPath.toString(), "--new-window", uri.toString()).start();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to open browser window.", e);
        }
    }
}