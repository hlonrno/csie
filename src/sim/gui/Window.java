package sim.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

import sim.world.World;
import sim.*;
import sim.gates.*;
import sim.tui.*;

public class Window {
    private static int FPS = 60;
    private JFrame frame;
    private Renderer renderer;
    private World world;
    private InputManager inputManager;
    private JLabel keyBuffer;
    
    public static void main(String[] args) throws InterruptedException {
        new Window().loop();
    }

    public Window() throws InterruptedException {
        frame = new JFrame();

        frame.setSize(800, 800);
        frame.setTitle("CM2.1");
        frame.setUndecorated(true);
        frame.setBackground(Colors.darkGrey.get());
        frame.setLayout(new BorderLayout());

        var label = new JLabel("Loading..");
        label.setBackground(Colors.darkGrey.get());
        label.setForeground(Colors.white.get());
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Comic Sans", Font.BOLD, 58));
        frame.add(label);
        frame.setVisible(true);
        frame.repaint();

        world = new World();
        inputManager = new InputManager();
        renderer = new Renderer(world);
        keyBuffer = new JLabel();

        keyBuffer.setFont(new Font("Comic Sans", Font.PLAIN, 14));
        keyBuffer.setPreferredSize(new Dimension(30, 20));

        //
        for (int i = -100; i < 100; i++)
            for (int j = -100; j < 100; j++)
                world.set(new Gate(GateType.OR, new Vec2(i, j)));
        world.iterable(Range.MAX_RANGE);
        //

        frame.addKeyListener(inputManager.getKeyListener());
        frame.add(renderer, BorderLayout.CENTER);
        frame.add(keyBuffer, BorderLayout.SOUTH);

        frame.remove(label);
    }

    public void loop() throws InterruptedException {
        long now;
        long nanos;
        long millis;
        long frameTimeNanos = 1_000_000_000l / FPS;
        long lastTime = System.nanoTime();
        float deltaTime;
        while (true) {
            if (!frame.isVisible()) {
                System.out.println("BAI!");
                System.exit(0);
            }
            now = System.nanoTime();
            deltaTime = (float)frameTimeNanos / (now - lastTime);

            /*int movementSpeed = (int)(8 * deltaTime);
            if (input.isKeyPressed("w")) {
                renderer.cameraPosition.add(0, -movementSpeed);
            }
            if (input.isKeyPressed("s")) {
                renderer.cameraPosition.add(0, movementSpeed);
            }
            if (input.isKeyPressed("a")) {
                renderer.cameraPosition.add(-movementSpeed, 0);
            }
            if (input.isKeyPressed("d")) {
                renderer.cameraPosition.add(movementSpeed, 0);
            }
            if (input.isKeyPressed("q")) {
                renderer.scale += 0.06f * deltaTime;
            }
            if (input.isKeyPressed("e")) {
                renderer.scale -= 0.06f * deltaTime;
            }*/

            keyBuffer.setText(" " + inputManager.getKeyBuffer() + renderer.viewRange);
            frame.repaint();

            lastTime = now;
            nanos = (frameTimeNanos - (System.nanoTime() - now)) / 1000;
            millis = nanos / 1000;
            nanos -= millis * 1000;
            if (millis > 0 && nanos > 0)
                Thread.sleep(millis, (int)nanos);
        }
    }
}
