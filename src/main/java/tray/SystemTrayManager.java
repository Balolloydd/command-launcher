package tray;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.util.Objects;

public final class SystemTrayManager implements AutoCloseable {
    private final Runnable onOpen;
    private final Runnable onExit;

    private SystemTray systemTray;
    private TrayIcon trayIcon;

    public SystemTrayManager(Runnable onOpen, Runnable onExit) {
        this.onOpen = Objects.requireNonNull(onOpen);
        this.onExit = Objects.requireNonNull(onExit);
    }

    public void start() {
        if (trayIcon != null) {
            throw new IllegalStateException("System tray manager is already running.");
        }

        if (!SystemTray.isSupported()) {
            throw new IllegalStateException("System tray is not supported.");
        }

        // Create context menu
        PopupMenu menu = new PopupMenu();

        MenuItem openItem = new MenuItem("Open CommandLauncher");
        MenuItem exitItem = new MenuItem("Exit CommandLauncher");

        openItem.addActionListener(event -> onOpen.run());
        exitItem.addActionListener(event -> onExit.run());

        menu.add(openItem);
        menu.addSeparator();
        menu.add(exitItem);

        // Create tray icon
        TrayIcon icon = new TrayIcon(createIcon(), "CommandLauncher", menu);
        icon.setImageAutoSize(true);

        // Double-clicking the icon opens the launcher
        icon.addActionListener(event -> onOpen.run());

        // Register icon with Windows
        SystemTray tray = SystemTray.getSystemTray();

        try {
            tray.add(icon);

            this.systemTray = tray;
            this.trayIcon = icon;

        } catch (AWTException e) {
            throw new IllegalStateException("Failed to add system tray icon.", e);
        }
    }

    private BufferedImage createIcon() {
        int size = 64;

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Icon background
            graphics.setColor(new Color(36, 40, 48));
            graphics.fillRoundRect(0, 0, size, size, 16, 16);

            // Draw the letter C
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Segoe UI", Font.BOLD, 40));

            FontMetrics metrics = graphics.getFontMetrics();
            String text = "C";

            int x = (size - metrics.stringWidth(text)) / 2;
            int y = (size - metrics.getHeight()) / 2 + metrics.getAscent();

            graphics.drawString(text, x, y);
        } finally {
            graphics.dispose();
        }

        return image;
    }

    @Override
    public void close() {
        if (trayIcon != null) {
            systemTray.remove(trayIcon);

            trayIcon = null;
            systemTray = null;
        }
    }
}