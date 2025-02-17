package sim.gui;

import javax.swing.JPanel;
import java.awt.Graphics;
import sim.Vec2;
import sim.world.World;

public class Renderer extends JPanel {
    public static final int cellSize = 32;
    public static final int padding = 2;
    public float scale = 1f;
    private final Vec2 one = Vec2.one();
    protected Vec2 cameraPosition = new Vec2(-32, -32),
              scaledCameraPosition = one.clone(),
              viewTopLeft = one.clone(), // in cells
              viewSize = one.clone();    // in cells
    private Vec2 topLeft = one.clone(),
            size = one.clone();
    private int scaledPadding;
    private World world;

    public Renderer(World world) {
        this.world = world;
    }

    /**
     * Changes the scale, and the camera's position
     * relative to this panel's center.
     * @param newScale (float) - the new scale.
     */
    public void changeScale(float newScale) {
        // TODO: change camera position accordingly.
        scale = newScale;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Colors.darkGrey.get());
        g.fillRect(0, 0, getWidth(), getHeight());
        scaledPadding = (int)(padding * scale);
        scaledCameraPosition.set(cameraPosition)
            .map(x -> (int)Math.floor(x * scale));

        viewTopLeft.set(scaledCameraPosition)
            .map(x -> (int)Math.floor(x / (cellSize * scale)));
        viewSize.set(scaledCameraPosition)
            .add(getWidth(), getHeight())
            .map(x -> (int)Math.ceil(x / (cellSize * scale)));

        for (var gate : world.iterable()) {
            topLeft.set(gate.position)
                .map(x -> (int)Math.floor(x * cellSize * scale));
            size.set(gate.position)
                .map(x -> (int)Math.floor((x + 1) * cellSize * scale) - 1)
                .sub(topLeft);
            topLeft.sub(scaledCameraPosition);

            g.setColor(Colors.black.get());
            g.fillRect(topLeft.x, topLeft.y, size.x, size.y);

            topLeft.add(scaledPadding);
            size.sub(scaledPadding << 1);

            g.setColor(gate.getColor());
            g.fillRect(topLeft.x, topLeft.y, size.x, size.y);
        }
    }
}
