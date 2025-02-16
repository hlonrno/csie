package sim.gui;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import sim.world.World;
import sim.Vec2;
import sim.gates.*;
import sim.tui.Input;

public class Window {
  private static int FPS = 60;
  private JFrame frame;
  private Renderer renderer;
  private World world;
  private Input input;
  private JLabel keyBuffer;

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
    world.add(new Gate(GateType.AND, new Vec2(-1, -1)));
    world.add(new Gate(GateType.OR, new Vec2(4, 4)));

    for (var g : world.gateIterable()) {
      System.out.println(g);
    }
    System.out.println(world.get(new Vec2(4, 4)));

    keyBuffer = new JLabel();
    frame.add(keyBuffer);

    renderer = new Renderer(world);
    frame.add(renderer);

    frame.remove(label);
    frame.repaint();

    input = new Input();
    frame.addKeyListener(input);
  }

  public void loop() throws InterruptedException {
    long now, frameTimeNanos = 1_000_000_000l / FPS, nanos, millis, lastTime = System.nanoTime();
    float deltaTime;
    while (true) {
      if (!frame.isVisible()) {
        System.out.println("BAI!");
        System.exit(0);
      }
      now = System.nanoTime();
      deltaTime = (float)frameTimeNanos / (now - lastTime);

      int movementSpeed = (int)(8 * deltaTime);
      if (input.isKeyPressed("w")) {
        renderer.cameraOffset.add(0, -movementSpeed);
      }
      if (input.isKeyPressed("s")) {
        renderer.cameraOffset.add(0, movementSpeed);
      }
      if (input.isKeyPressed("a")) {
        renderer.cameraOffset.add(-movementSpeed, 0);
      }
      if (input.isKeyPressed("d")) {
        renderer.cameraOffset.add(movementSpeed, 0);
      }
      if (input.isKeyPressed("q")) {
        renderer.scale += 0.06f * deltaTime;
      }
      if (input.isKeyPressed("e")) {
        renderer.scale -= 0.06f * deltaTime;
      }

      keyBuffer.setText(input.keyBuffer());

      frame.repaint();

      lastTime = now;
      nanos = (frameTimeNanos - (System.nanoTime() - now)) / 1000;
      millis = nanos / 1000;
      nanos -= millis * 1000;
      // System.out.printf("sleep: %3d.%3dms\n", millis, nanos);
      if (millis + nanos > 0)
        Thread.sleep(millis, (int)nanos);
      else
        Thread.yield();
    }
  }
}
