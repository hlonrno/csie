package sim.tui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {
  public static final String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ";
  private StringBuffer keyBuffer = new StringBuffer("");
  
  public Input() {}

  public String keyBuffer() {
    return keyBuffer.toString();
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
      } else
        key = String.valueOf(chr);

      if (!isKeyPressed(key))
        return false;
    }

    return true;
  }

  public String getKeyString(int keyCode) {
    if (ALLOWED_CHARS.indexOf((char)keyCode) != -1)
      return String.valueOf((char)keyCode).toLowerCase();

    return switch (keyCode) {
      case KeyEvent.VK_ALT -> "\\A";
      case KeyEvent.VK_WINDOWS-> "\\W";
      case KeyEvent.VK_CONTROL -> "\\C";
      case KeyEvent.VK_SHIFT -> "\\S";
      case KeyEvent.VK_TAB -> "\\T";
      case KeyEvent.VK_ENTER -> "\\<";
      case KeyEvent.VK_BACK_SPACE -> "\\>";
      case KeyEvent.VK_ESCAPE -> "\\E";
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
      case KeyEvent.VK_CAPS_LOCK -> "\\L";
      default -> {
        System.out.printf("""
          [WARN]: Unrecognized Key.
            keycode: %d
            char: '%c'
            text: "%s"\n
          """,
          keyCode,
          (char)keyCode,
          KeyEvent.getKeyText(keyCode)
        );
        yield "[null]";
      }
    };
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    String key = getKeyString(e.getKeyCode());
    if (!isKeyPressed(key))
      keyBuffer.append(key);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    String key = getKeyString(e.getKeyCode());
    int index = keyBuffer.indexOf(key);
    if (index != -1)
      keyBuffer.delete(
        index,
        index + key.length()
      );
  }
}
