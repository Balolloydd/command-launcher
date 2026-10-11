package browser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultBrowserResolver {

    private static final String ASSOCIATION_KEY ="HKCU\\Software\\Microsoft\\Windows\\Shell\\Associations\\UrlAssociations\\https";

    public Path resolve() {
        String progId;

        try {
            progId = readRegistry(ASSOCIATION_KEY + "\\UserChoiceLatest\\ProgId", "ProgId");
        } catch (IllegalStateException e) {
            progId = readRegistry(ASSOCIATION_KEY + "\\UserChoice", "ProgId");
        }

        if (!progId.matches("[A-Za-z0-9._-]+")) {
            throw new IllegalStateException("Invalid browser association.");
        }

        String command = readRegistry("HKCR\\" + progId + "\\shell\\open\\command",null);
        Matcher matcher = Pattern.compile("^\\s*\"([^\"]+\\.exe)\"", Pattern.CASE_INSENSITIVE).matcher(command);
        
        if (!matcher.find()) {
            throw new IllegalStateException("Could not resolve default browser executable.");
        }

        return Path.of(matcher.group(1));
    }

    private String readRegistry(String key, String valueName) {
        List<String> args = new ArrayList<>(List.of("reg", "query", key));

        if (valueName == null) {
            args.add("/ve");
        } else {
            args.add("/v");
            args.add(valueName);
        }

        try {
            Process process = new ProcessBuilder(args).redirectErrorStream(true).start();
            String output = new String(process.getInputStream().readAllBytes(), Charset.defaultCharset());
            
            if (process.waitFor() != 0) {
                throw new IllegalStateException("Registry query failed: " + key);
            }

            Matcher matcher = Pattern.compile("(?m)^\\s*.+?\\s+REG_(?:SZ|EXPAND_SZ)\\s+(.+)$").matcher(output);
            if (!matcher.find()) {
                throw new IllegalStateException("Registry value not found: " + key);
            }

            return matcher.group(1).trim();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read Windows Registry.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Registry lookup interrupted.", e);
        }
    }
}