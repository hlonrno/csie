package sim;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Window {
  private JFrame frame;

  public Window() {
    frame = new JFrame();

    frame.setSize(800, 800);
    frame.setTitle("CM2.1");
    frame.setUndecorated(true);
    frame.setBackground(Colors.darkGrey.get());
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setLayout(null);

    var panel = new JPanel();
    panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
    frame.add(panel);
    var text = new JLabel("Loading..");
    panel.add(text);

    frame.setVisible(true);

    try { Thread.sleep(2000); } catch (Exception e) {}

    frame.remove(panel);
    frame.repaint();
  }
}
