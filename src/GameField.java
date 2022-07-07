import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


public class GameField extends JPanel implements ActionListener {
    public final Timer timer;
    private final Player player1 = new Player("Игрок 1");
    private final Player p2 = new Player("Игрок 2");
    private final Player[] players = new Player[]{player1,p2};
    private final GameGraphics draw = new GameGraphics();
    public int cursorX = 310;
    public int cursorY = 250;
    public int[] shipX = new int[4];
    public int[] shipY = new int[4];

    private boolean StartGame = true;
    private boolean CreateUsers = false;
    private boolean BattleGame = false;

    public GameField() {
        setBackground(Color.BLACK);
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
        timer = new Timer(250, this);
        timer.start();
    }

    public boolean checkCollision(int[] x, int[] y, int endShip) {
        if (x[0] == 0) return false;

        for (int i = 0; i < endShip ; i++) if (player1.checkShip(x[i], y[i])) return false;

        if (x[0] == x[endShip]) {
            for (int i = y[0]; i < y[endShip] + 1; i += 28) {
                if (x[0] - 28 > 39) if (player1.checkShip(x[0] - 28, i)) return false;
                if (x[0] + 28 < 293) if (player1.checkShip(x[0] + 28, i)) return false;
            }
            if (y[0] - 28 > 24) if (player1.checkShip(x[0], y[0] - 28)) return false;
            if (y[endShip] + 28 < 278) {
                if (player1.checkShip(x[0], y[endShip] + 28)) return false;
            }
            if (y[0] - 28 > 24 && x[0] - 28 > 39) if (player1.checkShip(x[0] - 28, y[0] - 28)) return false;
            if (y[0] - 28 > 24 && x[0] + 28 < 293) if (player1.checkShip(x[0] + 28, y[0] - 28)) return false;
            if (y[endShip] + 28 < 278 && x[0] + 28 < 293)
                if (player1.checkShip(x[0] + 28, y[endShip] + 28)) return false;
            if (y[endShip] + 28 < 278 && x[0] - 28 > 39)
                if (player1.checkShip(x[0] - 28, y[endShip] + 28)) return false;
        }
        if (y[0] == y[endShip]) {
            for (int i = x[0]; i < x[endShip] + 1; i += 28) {
                if (y[0] - 28 > 24) if (player1.checkShip(i, y[0] - 28)) return false;
                if (y[0] + 28 < 278) if (player1.checkShip(i, y[0] + 28)) return false;
            }
            if (x[0] - 28 > 39) if (player1.checkShip(x[0] - 28, y[0])) return false;
            if (x[0] + 28 < 293) if (player1.checkShip(x[endShip] + 28, y[0])) return false;

            if (y[0] - 28 > 24 && x[0] - 28 > 39) if (player1.checkShip(x[0] - 28, y[0] - 28)) return false;
            if (y[0] - 28 > 24 && x[endShip] + 28 < 293)
                if (player1.checkShip(x[endShip] + 28, y[0] - 28)) return false;
            if (y[0] + 28 < 278 && x[endShip] + 28 < 293)
                if (player1.checkShip(x[endShip] + 28, y[0]+28)) return false;
            if (y[0] + 28 < 278 && x[0] - 28 > 39)
                if (player1.checkShip(x[0] - 28, y[0] + 28)) return false;
        }
        return true;
    }

    public void moveShip(String s, int lengthShip) {
        if ("left".equals(s) && shipX[0] > 40)
            for (int i = 0; i < lengthShip; i++) shipX[i] -= 28;
        if ("right".equals(s) && shipX[0] < 292 && shipX[lengthShip - 1] < 292)
            for (int i = 0; i < lengthShip; i++) shipX[i] += 28;
        if ("up".equals(s) && shipY[0] > 40 && shipY[lengthShip - 1] > 40)
            for (int i = 0; i < lengthShip; i++) shipY[i] -= 28;
        if ("down".equals(s) && shipY[lengthShip - 1] < 256 && shipY[0] < 256)
            for (int i = 0; i < lengthShip; i++) shipY[i] += 28;

        if ("enter".equals(s)) {
            player1.nextShip();
            player1.writeShip(shipX, shipY, 1);
            for (int i = 0; i < shipY.length; i++) {
                shipY[i] = 0;
                shipX[i] = 0;
            }
        }

        if ("space".equals(s)) {
            for (int i = 1; i < lengthShip; i++) {
                if (shipX[0] == shipX[lengthShip - 1]) {
                    shipY[i] = shipY[0];
                    shipX[i] = shipX[0] + (i * 28);
                    if (shipX[i] > 292) for (int j = lengthShip - 1; j != -1; j--) shipX[j] = shipX[j] - 28;
                } else {
                    shipX[i] = shipX[0];
                    shipY[i] = shipY[0] + (i * 28);
                    if (shipY[i] < 40) for (int j = lengthShip - 1; j != -1; j--) shipY[j] = shipY[j] + 28;
                }
            }
        }
    }

    private void createPlayer(Graphics g) {

        draw.gameTable(g, 1, player1);
        draw.countShips(g, player1.numberShips);

        for (int i = 0; i < player1.getShip(); i++) {
            if (shipX[i] == 0 && shipY[i] == 0) {
                shipX[i] = 40;
                shipY[i] = 25 + (i * 28);
            }
            g.drawImage(draw.ship, shipX[i], shipY[i], this);
        }


    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (StartGame) draw.startGame(g, cursorX, cursorY);
        if (CreateUsers) createPlayer(g);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();

            if (StartGame) {
                if (key == KeyEvent.VK_DOWN && cursorY < 290) cursorY += 40;
                if (key == KeyEvent.VK_UP && cursorY > 250) cursorY -= 40;
                if (key == KeyEvent.VK_ENTER && cursorY == 290) System.exit(0);
                if (key == KeyEvent.VK_ENTER && cursorY == 250) {
                    StartGame = false;
                    CreateUsers = true;
                }
            }
            if (CreateUsers) {
                if (key == KeyEvent.VK_LEFT) moveShip("left", player1.getShip());
                if (key == KeyEvent.VK_RIGHT) moveShip("right", player1.getShip());
                if (key == KeyEvent.VK_DOWN) moveShip("down", player1.getShip());
                if (key == KeyEvent.VK_UP) moveShip("up", player1.getShip());
                if (key == KeyEvent.VK_SPACE) moveShip("space", player1.getShip());
                if (key == KeyEvent.VK_ENTER && checkCollision(shipX, shipY, player1.getShip()-1)) moveShip("enter", player1.getShip());

            }
        }
    }

}
