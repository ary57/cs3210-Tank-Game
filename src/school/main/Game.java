package school.main;

import school.main.gameobjects.GameBox;
import school.main.gameobjects.GameMap;
import school.main.gameobjects.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Game extends JComponent implements Runnable, KeyListener {

    public static void main(String[] args) {
        JFrame window = Game.MainFrame(700, 450);

    }

    private final Thread updateThread;
    protected HashMap<String, GameObject> gameObjects = new HashMap();
    protected BufferedImage bufferImg;
    protected Graphics2D buffer;

    public static JFrame MainFrame(int width, int height) {
        JFrame out = new JFrame();
        out.setSize(width, height);
        out.setLocationRelativeTo(null);
        out.setTitle("CS:3210 Final Project");
        out.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        out.setVisible(true);
        Game window = new Game(width, height);
        window.init(out);
        out.setContentPane(window);
        out.addKeyListener(window);
        return out;
    }

    public Game(int width, int height) {
        this.setSize(width, height);

        this.setVisible(true);
        this.setupBuffer(width, height);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                System.out.println("Resized");
                setupBuffer(getWidth(), getHeight());
            }
        });
        this.updateThread = new Thread(this);
        this.updateThread.start();
    }

    protected void init(JFrame frame) {
        GameMap map = new GameMap(frame, this);
GameBox.PlaceBox(map, 100, 200, 20, Color.green);
        GameBox.PlaceBox(map, 300, 300, 20, Color.magenta);
        GameBox.PlaceBox(map, 330, 300, 20, Color.CYAN);
        this.addObject(map);

    }

    private void setupBuffer(int width, int height) {
        bufferImg = new BufferedImage((int) Math.ceil(width * resolutionScale), (int) Math.ceil(height * resolutionScale), BufferedImage.TYPE_4BYTE_ABGR);
        buffer = bufferImg.createGraphics();
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        buffer.setRenderingHints(rh);
    }

    public void addObject(GameObject obj) {
        this.gameObjects.put(obj.getId(), obj);

    }


    private double deltaMs;

    private int fpsTarget = 60;
    private double resolutionScale = 1;

    private double getFrameDelay() {
        return fpsTarget / 100.0;
    }


    private GameObject currentGameObject = null;
    private AffineTransform oldTransform;

    public void paint(Graphics graphics) {
        if (buffer != null) {

            buffer.setColor(Color.red);
            buffer.fillRect(0, 0, this.getWidth(), this.getHeight());
            oldTransform = buffer.getTransform();
            for (String id : this.gameObjects.keySet()) {
                currentGameObject = this.gameObjects.get(id);
                buffer.translate(currentGameObject.getX(), currentGameObject.getY());
                buffer.rotate(currentGameObject.getRotation(), currentGameObject.getRotationOffsetX(), currentGameObject.getRotationOffsetY());
                currentGameObject.draw(buffer, deltaMs);
                buffer.setTransform(oldTransform);
                //graphics.translate(0-currentGameObject.getX(), 0-currentGameObject.getY());

            }
            graphics.setColor(Color.green);
            buffer.drawString(deltaMs + "ms", 16, this.getHeight() - 16);
            graphics.drawImage(bufferImg, 0, 0, getWidth(), getHeight(), null);

        } else {
            invalidate();
        }
    }

    @Override
    public void run() {
        long current = System.currentTimeMillis();
        long last = current;
        long delta;
        double frameDelay = this.getFrameDelay();
        while (true) {
            current = System.currentTimeMillis();
            delta = current - last;
            if (delta >= frameDelay) {
                this.deltaMs = delta;
                this.repaint();
                // System.out.println(delta);
                last = current;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        GameObject obj;
        for (String id : this.gameObjects.keySet()) {
            obj = this.gameObjects.get(id);
            if(obj instanceof KeyListener){
                ((KeyListener) obj).keyTyped(e);
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        GameObject obj;
        for (String id : this.gameObjects.keySet()) {
            obj = this.gameObjects.get(id);
            if(obj instanceof KeyListener){
                ((KeyListener) obj).keyPressed(e);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        GameObject obj;
        for (String id : this.gameObjects.keySet()) {
            obj = this.gameObjects.get(id);
            if(obj instanceof KeyListener){
                ((KeyListener) obj).keyReleased(e);
            }
        }
    }
}
