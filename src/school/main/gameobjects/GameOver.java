package school.main.gameobjects;

import school.main.GameObject;

import javax.swing.*;
import java.awt.*;

public class GameOver extends GameObject<GameMap> {
    public GameOver( String whoLost, GameMap parent) {
        super("GameOver", parent);
        this.mapReference = parent;
        this.whoLostText = whoLost + " Lost!";
        this.setSize(this.getFrame().getWidth() / 2, this.getFrame().getHeight() / 2);
        parent.addChildObject(this);

    }
    private GameMap mapReference;

    @Override
    public JFrame getFrame() {
        return mapReference.getFrame();
    }
    // whoLostText : text representing the player who lost the match.
    private String whoLostText = null;
    private Font font = new Font("Ink Free", Font.BOLD, 50);

    @Override
    public void onDraw(Graphics graphics, double deltaMs) {
        graphics.setFont(font);
        int x = -1 * (int) getGlobalX() + (int) getWidth() / 2;
        int y = -1 * (int) getGlobalY() + (int) getHeight() / 2;
        graphics.setColor(Color.black);
        graphics.fillRect((int) getX(), 0, (int) getFrame().getWidth(), (int) getFrame().getHeight());
        graphics.setColor(Color.yellow);
        // graphics.fillRect(0, 0, (int) getWidth(), (int) getHeight());
        graphics.drawString("Game Over", x, y);
        if (whoLostText != null) {
            graphics.drawString(whoLostText, getFrame().getWidth() / 2 - graphics.getFontMetrics(font).stringWidth(whoLostText) / 2, y + 75);
        }
    }
}
