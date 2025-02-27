package sim.tui;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Optional;

public class InputManager {
    private static final String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ";
    private StringBuffer keyBuffer = new StringBuffer("");
    private Thread debounce;

    public InputManager() {
        debounce = Thread.ofPlatform().unstarted(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    clearKeyBuffer();
                    debounce.interrupt();
                } catch (Exception e) {}
            }
        });
    }

    public String getKeyBuffer() {
        return keyBuffer.toString();
    }

    public  void clearKeyBuffer() {
        keyBuffer.delete(0, keyBuffer.length());
    }

    public boolean isKeyPressed(String key) {
        return keyBuffer.indexOf(key) != -1;
    }

    public boolean areKeysPressed(String keys) {
        for (int i = 0; i < keys.length(); i++) {
            String key;
            char chr = keys.charAt(i);
            if (chr == '\\') {
                i++;
                key = String.valueOf(chr) + keys.charAt(i);
            } else {
                key = String.valueOf(chr);
            }

            if (!isKeyPressed(key))
                return false;
        }

        return true;
    }

    public Optional<String> getKeyString(int keyCode) {
        if (ALLOWED_CHARS.indexOf((char)keyCode) != -1)
            return Optional.of(String.valueOf((char)keyCode).toLowerCase());

        return Optional.ofNullable(switch (keyCode) {
            case KeyEvent.VK_ALT -> "\\A";
            case KeyEvent.VK_WINDOWS-> "\\W";
            case KeyEvent.VK_CONTROL -> "\\C";
            case KeyEvent.VK_SHIFT -> "\\S";
            case KeyEvent.VK_TAB -> "\\T";
            case KeyEvent.VK_ENTER -> "\\<";
            case KeyEvent.VK_BACK_SPACE -> "\\>";
            case KeyEvent.VK_INSERT -> "\\I";
            case KeyEvent.VK_QUOTE -> "\'";
            case KeyEvent.VK_SEMICOLON -> ";";
            case KeyEvent.VK_OPEN_BRACKET -> "[";
            case KeyEvent.VK_CLOSE_BRACKET -> "]";
            case KeyEvent.VK_COMMA-> ",";
            case KeyEvent.VK_PERIOD -> ".";
            case KeyEvent.VK_SLASH -> "/";
            case KeyEvent.VK_BACK_SLASH -> "\\\\";
            case KeyEvent.VK_BACK_QUOTE -> "`";
            case KeyEvent.VK_LEFT -> "\\H";
            case KeyEvent.VK_DOWN -> "\\J";
            case KeyEvent.VK_UP -> "\\K";
            case KeyEvent.VK_RIGHT -> "\\L";
            case KeyEvent.VK_ESCAPE -> "\\E";
            default -> {
                System.out.printf("""
                    [WARN]: Unrecognized Key.
                        keycode: %d, 
                        char: '%c',
                        text: "%s".\n
                    """,
                    keyCode,
                    (char)keyCode,
                    KeyEvent.getKeyText(keyCode)
                );
                yield null;
            }
        });
    }

    public void keyPressed(KeyEvent e) {
        var key = getKeyString(e.getKeyCode());
        if (!key.isPresent() && isKeyPressed(key.get()))
            return;
        keyBuffer.append(key.get());
        debounce.interrupt();
    }

    public KeyAdapter getKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                var key = getKeyString(e.getKeyCode());
                if (!key.isPresent() && isKeyPressed(key.get()))
                    return;
                keyBuffer.append(key.get());
                debounce.interrupt();
            }
        };
    }
}
