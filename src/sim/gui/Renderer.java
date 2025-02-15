package sim.gui;

import javax.swing.JPanel;
import java.awt.Graphics;
import sim.Vec2;
import sim.gates.Gate;
import sim.world.World;

public class Renderer extends JPanel {
  public static int cellSize = 32;
  public static float scale = 1f;
  private static final int PADDING = 2;
  private World world;
  private final Vec2 ONE = Vec2.one();
  private Vec2 topLeft = ONE.clone(), size = ONE.clone();
  protected Vec2 cameraOffset = new Vec2(-32, -32), scaledCameraOffset = new Vec2(0);

  public Renderer(World world) {
    this.world = world;
  }

  @Override
  protected void paintComponent(Graphics g) {
    g.setColor(Colors.darkGrey.get());
    g.fillRect(0, 0, getWidth(), getHeight());
    final int SCALED_PADDING = (int) (PADDING * scale);
    scaledCameraOffset.set(cameraOffset)
      .map(x -> (int)Math.floor(x * scale));

    for (Gate gate : world.gateIterable()) {
      topLeft.set(gate.position)
          .map(x -> (int)Math.floor(x * cellSize * scale));
      size.set(gate.position)
          .map(x -> (int)Math.floor((x + 1) * cellSize * scale) - 1)
          .sub(topLeft);
      topLeft.sub(scaledCameraOffset);

      g.setColor(Colors.black.get());
      g.fillRect(topLeft.x, topLeft.y, size.x, size.y);

      topLeft.add(SCALED_PADDING);
      size.sub(SCALED_PADDING << 1);

      g.setColor(gate.getColor());
      g.fillRect(topLeft.x, topLeft.y, size.x, size.y);
    }
  }
}
