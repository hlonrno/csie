package sim.gui;

import javax.swing.JPanel;
import java.awt.Graphics;
import sim.world.World;
import sim.Range;
import sim.Vec2;

public class Renderer extends JPanel {
    public static final int cellSize = 32;
    public static final int padding = 2;
    public float scale = 1f;
    private final Vec2 one = Vec2.one();
    protected Vec2 cameraPosition = new Vec2(0, 0);
    private Vec2 viewTopLeft = one.clone(), // in cells
            viewSize = one.clone();    // in cells
    public Range viewRange;
    private Vec2 topLeft = one.clone(),
            size = one.clone();
    private int scaledPadding;
    private World world;

    public Renderer(World world) {
        this.world = world;
        viewRange = new Range(0, 0, 0, 0);
        viewRange.position = viewTopLeft;
        viewRange.size = viewSize;
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

        // viewRange has references to these objects.
        viewTopLeft.set(cameraPosition)
            .map(x -> (int)(x / (cellSize * scale) - (x < 0 ? 1f : 0f)));
        viewSize.set(cameraPosition)
            .add(getWidth(), getHeight())
            .map(x -> (int)(x / (cellSize * scale) + 1f))
            .sub(viewTopLeft);

        for (var gate : world.iterable(viewRange)) {
            topLeft.set(gate.position)
                .map(x -> (int)(x * cellSize * scale));
            size.set(gate.position)
                .map(x -> (int)((x + 1) * cellSize * scale) - 1)
                .sub(topLeft);
            topLeft.sub(cameraPosition);

            g.setColor(gate.getColor());
            g.fillRect(topLeft.x, topLeft.y, size.x, size.y);
        }
    }
}
