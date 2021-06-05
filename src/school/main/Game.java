package school.main;

import school.main.gameobjects.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class Game extends JComponent implements Runnable, KeyListener {

    public static void main(String[] args) {
        JFrame window = Game.MainFrame(700, 450);
    }

    //List of game objects to be drawn
    protected HashMap<String, GameObject> gameObjects = new HashMap();

    public void addGameObject(GameObject obj) {
        this.gameObjects.put(obj.getId(), obj);
    }

    //bufferImage used for double buffering
    protected BufferedImage bufferImg;
    //buffer = bufferImage.getGraphics()
    protected Graphics2D buffer;

    public static JFrame MainFrame(int width, int height) {
        JFrame out = new JFrame();
        out.setSize(width, height);
        out.setResizable(false);
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


    private BufferedImage backgroundImage;
    public Game(int width, int height) {
        this.setSize(width, height);

        this.setVisible(true);
        this.setupBuffer(width, height);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
//                System.out.println("Resized");
                setupBuffer(getWidth(), getHeight());
            }
        });
        this.lastPaintTime = new Date().getTime();

        File bgImage = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "background_flag_of_thailand.png");
        try {
            backgroundImage = ImageIO.read(bgImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread updateThread = new Thread(this);
        updateThread.start();
    }

    /**
     * This sets up the GameMap, which sets up the players
     * @param frame JFrame to initialize game in
     */
    protected void init(JFrame frame) {
        this.map = new GameMap(frame, this);
        this.addGameObject(map);
    }


    /**
     * Drawing directly to JFrame paint() can result in weird flickering.
     * Drawing to a separate frame buffer, then drawing the buffer's image resolves this.
     *
     * @param width  width of frame buffer to create
     * @param height height of frame buffer
     */
    private void setupBuffer(int width, int height) {
        bufferImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        buffer = bufferImg.createGraphics();
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        buffer.setRenderingHints(rh);
    }


    public int fpsTarget = 60;


    private double getFrameDelay() {
        return fpsTarget / 100.0;
    }

//drawCollisions is a debug flag, turn on to view collision geometry
    private boolean drawCollisions = false;


    private boolean isGameRunning;
    public GameMap map;

    public void gameOver(String whoWone) {
        isGameRunning = false;
        GameOver go = new GameOver(whoWone, map);
        map.mapSpeed = 0;
        go.setX(-map.getX() / 2);
    }

    //lastPaintTime and currentPaintTime are used to calculate deltaMs (milliseconds between frames)
    private long lastPaintTime, currentPaintTime;
    //deltaMs, fpsFont, currentGameObject, & oldTransform could all be local variables of paint(),
    //but they are kept private fields to avoid memory churn
    private double deltaMs;
    private Font fpsFont = new Font("Courier", Font.BOLD, 16);
    private GameObject currentGameObject = null;
    private AffineTransform oldTransform;

    public void paint(Graphics graphics) {
        if (buffer != null) {
            currentPaintTime = new Date().getTime();
            deltaMs = currentPaintTime - lastPaintTime;
//            buffer.setColor(Color.red);
//            buffer.fillRect(0, 0, this.getWidth(), this.getHeight());
            oldTransform = buffer.getTransform();
            buffer.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            for (String id : this.gameObjects.keySet()) {
                currentGameObject = this.gameObjects.get(id);
                buffer.translate(currentGameObject.getX(), currentGameObject.getY());
                buffer.rotate(currentGameObject.getRotation(), currentGameObject.getRotationOffsetX(), currentGameObject.getRotationOffsetY());
                currentGameObject.draw(buffer, deltaMs);
                buffer.setTransform(oldTransform);
                //graphics.translate(0-currentGameObject.getX(), 0-currentGameObject.getY());

            }
            if (this.drawCollisions) {
                for (String id : this.gameObjects.keySet()) {
                    currentGameObject = this.gameObjects.get(id);
                    currentGameObject.drawCollisions(buffer, deltaMs);

                }
            }

            buffer.setColor(Color.green);
            buffer.setFont(fpsFont);
            buffer.drawString(deltaMs + "ms", 16, 16 + fpsFont.getSize());

            graphics.drawImage(bufferImg, 0, 0, getWidth(), getHeight(), null);
            this.lastPaintTime = this.currentPaintTime;
        } else {
            invalidate();
        }
    }

    /**
     * This is the core of the game loop. It is called in a separate thread onInit,
     * and is responsible for driving repaint() every
     */
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
            if (obj instanceof KeyListener) {
                ((KeyListener) obj).keyTyped(e);
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        GameObject obj;
        for (String id : this.gameObjects.keySet()) {
            obj = this.gameObjects.get(id);
            if (obj instanceof KeyListener) {
                ((KeyListener) obj).keyPressed(e);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        GameObject obj;
        for (String id : this.gameObjects.keySet()) {
            obj = this.gameObjects.get(id);
            if (obj instanceof KeyListener) {
                ((KeyListener) obj).keyReleased(e);
            }
        }
    }
}
