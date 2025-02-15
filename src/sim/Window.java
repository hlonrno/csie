package sim;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import sim.world.World;
import sim.gates.*;

public class Window {
  private static int FPS = 60;
  private JFrame frame;
  private Renderer renderer;
  private World world;

  public Window() {
    frame = new JFrame();

    frame.setSize(800, 800);
    frame.setTitle("CM2.1");
    frame.setUndecorated(true);
    frame.setBackground(Colors.darkGrey.get());
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    var label = new JLabel("Loading..");
    label.setVerticalAlignment(JLabel.CENTER);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setFont(new Font("Halvetica", Font.BOLD, 48));
    frame.add(label);
    frame.setVisible(true);

    world = new World();
    world.add(new Gate(GateType.AND, new Vec2(1, -1)));
    world.add(new Gate(GateType.AND, new Vec2(2, -2)));
    world.add(new Gate(GateType.AND, new Vec2(3, -3)));
    world.add(new Gate(GateType.AND, new Vec2(4, -4)));

    renderer = new Renderer(world);
    frame.add(renderer);

    frame.remove(label);
    frame.repaint();
  }

  public void loop() throws InterruptedException {
    long now, frameTimeNanos = 1_000_000_000l / FPS, nanos, millis;
    while (true) {
      if (!frame.isVisible()) {
        System.out.println("BAI!");
        System.exit(0);
      }
      now = System.nanoTime();

      frame.repaint();

      nanos = (frameTimeNanos - (System.nanoTime() - now)) / 1000;
      millis = nanos / 1000;
      nanos -= millis * 1000;
      System.out.printf("sleep: %3d.%3dms\n", millis, nanos);
      if (millis + nanos > 0)
        Thread.sleep(millis, (int)nanos);
      else
        Thread.yield();
    }
  }
}
