package hotkey;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public final class GlobalHotkeyManager implements AutoCloseable {
    private static final int HOTKEY_ID = 1;
    private static final int VK_SPACE = 0x20;

    private final Runnable action;

    private Thread listenerThread;
    private volatile int nativeThreadId;

    public GlobalHotkeyManager(Runnable action) {
        this.action = Objects.requireNonNull(action);
    }

    public void start() {
        if (listenerThread != null) {
            throw new IllegalStateException("Hotkey manager has already been started.");
        }

        CompletableFuture<Void> ready = new CompletableFuture<>();
        listenerThread = new Thread(() -> listen(ready), "commandlauncher-hotkey");

        listenerThread.setDaemon(true);
        listenerThread.start();

        try {
            ready.join();
        } catch (CompletionException e) {
            throw new IllegalStateException("Failed to initialize global hotkey.", e.getCause());
        }
    }

    private void listen(CompletableFuture<Void> ready) {
        boolean registered = false;

        try {
            registered = User32.INSTANCE.RegisterHotKey(null, HOTKEY_ID, WinUser.MOD_ALT | WinUser.MOD_NOREPEAT, VK_SPACE);

            if (!registered) {
                throw new IllegalStateException("Cannot register Alt + Space. Windows error: " + Kernel32.INSTANCE.GetLastError());
            }

            WinUser.MSG message = new WinUser.MSG();

            // Ensure the Windows message queue exists.
            User32.INSTANCE.PeekMessage(message, null, 0, 0, 0);

            nativeThreadId = Kernel32.INSTANCE.GetCurrentThreadId();
            ready.complete(null);

            int result;
            while ((result = User32.INSTANCE.GetMessage(message, null, 0, 0)) > 0) {
                if (message.message == WinUser.WM_HOTKEY && message.wParam.intValue() == HOTKEY_ID) {
                    action.run();
                }
            }

            if (result == -1) {
                System.err.println("Hotkey message loop failed. Windows error: " + Kernel32.INSTANCE.GetLastError());
            }
        } catch (RuntimeException | LinkageError e) {
            if (!ready.completeExceptionally(e)) {
                System.err.println("Hotkey listener failed: " + e.getMessage());
            }
        } finally {
            if (registered) {
                User32.INSTANCE.UnregisterHotKey(null, HOTKEY_ID);
            }

            nativeThreadId = 0;
        }
    }

    @Override
    public void close() {
        int threadId = nativeThreadId;

        if (threadId != 0) {
            User32.INSTANCE.PostThreadMessage(threadId, WinUser.WM_QUIT, new WinDef.WPARAM(0), new WinDef.LPARAM(0));
        }
    }
}