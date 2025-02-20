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
    private Range viewRange;
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

    private int dynamicFloor(float v) {
        return (int)(v < 0 ? Math.ceil(v) : Math.floor(v));
    }

    private int dynamicCeil(float v) {
        return (int)(v < 0 ? Math.floor(v) : Math.ceil(v));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Colors.darkGrey.get());
        g.fillRect(0, 0, getWidth(), getHeight());
        scaledPadding = (int)(padding * scale);

        // viewRange has references to these objects.
        viewTopLeft.set(cameraPosition)
            .map(x -> dynamicFloor(x / (cellSize * scale)));
        viewSize.set(cameraPosition)
            .add(getWidth(), getHeight())
            .map(x -> dynamicCeil(x / (cellSize * scale)))
            .sub(viewTopLeft);

        for (var gate : world.iterable(viewRange)) {
            topLeft.set(gate.position)
                .map(x -> (int)Math.floor(x * cellSize * scale));
            size.set(gate.position)
                .map(x -> (int)Math.floor((x + 1) * cellSize * scale) - 1)
                .sub(topLeft);
            topLeft.sub(cameraPosition);

            g.setColor(Colors.black.get());
            g.fillRect(topLeft.x, topLeft.y, size.x, size.y);

            topLeft.add(scaledPadding);
            size.sub(scaledPadding << 1);

            g.setColor(gate.getColor());
            g.fillRect(topLeft.x, topLeft.y, size.x, size.y);
        }
    }
}
