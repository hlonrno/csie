package sim.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
        label.setFont(new Font("", Font.BOLD, 58));
        frame.add(label);
        frame.setVisible(true);

        world = new World();
        input = new Input();
        renderer = new Renderer(world);
        keyBuffer = new JLabel();

        keyBuffer.setBackground(Colors.darkerGrey.get());
        keyBuffer.setForeground(Colors.white.get());
        keyBuffer.setFont(new Font("", Font.PLAIN, 14));
        keyBuffer.setPreferredSize(new Dimension(30, 20));

        //
        for (int i = 0; i < 25; i++)
            for (int j = 0; j < 25; j++)
                world.set(new Gate(GateType.OR, new Vec2(i, j)));
        world.iterable();
        //

        frame.addKeyListener(input);
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

            int movementSpeed = (int)(8 * deltaTime);
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
            }

            keyBuffer.setText(" " + input.getKeyBuffer());
            System.out.print("\n\n\n\n\n\n\n\n\n\n".repeat(4));
            frame.repaint();

            lastTime = now;
            nanos = (frameTimeNanos - (System.nanoTime() - now)) / 1000;
            millis = nanos / 1000;
            nanos -= millis * 1000;
            if (millis + nanos > 0)
                Thread.sleep(millis, (int)nanos);
            else
                Thread.yield();
        }
    }
}
