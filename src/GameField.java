import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;


public class GameField extends JPanel implements ActionListener {
    public final Timer timer;
    private final Player p1 = new Player("Player 1", 1);
    private final Player p2 = new Player("Player 2", 0);
    private Player player = p1;
    private Player opponent = p2;
    private final GameGraphics draw = new GameGraphics();
    public int cursorX = 310;
    public int cursorY = 250;
    public int[] shipX = new int[4];
    public int[] shipY = new int[4];
    private boolean blocking = true;
    private boolean StartGame = true;
    private boolean CreateUsers = false;
    private boolean BattleGame = false;
    private boolean EndGame = false;

    public GameField() {
        setBackground(Color.BLACK);
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
        timer = new Timer(250, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (StartGame) draw.startGame(g, cursorX, cursorY);
        if (CreateUsers) createPlayer(g);
        if (BattleGame) battleGame(g);
        if (EndGame) gameOver(g);
    }

    private void createPlayer(Graphics g) {
        player = !p1.readyToPlay ? p1 : p2;
        if (p1.readyToPlay && p2.readyToPlay) {
            CreateUsers = false;
            BattleGame = true;
        }
        if (player.getShip() == 0) player.readyToPlay = true;

        draw.gameTable(g, 1, player, false);
        draw.countShips(g, player);
        int boxStep = 25;
        for (int i = 0; i < player.getShip(); i++) {
            if (shipX[i] == 0 && shipY[i] == 0) {
                shipX[i] = 40;
                shipY[i] = boxStep;
                boxStep += 28;
            }

            g.drawImage(draw.ship, shipX[i], shipY[i], this);
        }
        blocking = true;
    }

    private void battleGame(Graphics g) {
        player = p1.numberOfActions > 0 ? p1 : p2;
        opponent = p1.numberOfActions == 0 ? p1 : p2;
        if (p1.allShips == 0 || p2.allShips == 0) {
            cursorX = 400;
            cursorY = 220;
            BattleGame = false;
            EndGame = true;
        }
        if (!player.onlyUser) draw.introPlayer(g, player);
        else {
            draw.gameTable(g, 1, player, true);
            draw.gameTable(g, 2, player, false);
            draw.info(g, opponent);
            draw.drawAim(g, cursorX, cursorY);
        }
    }

    private void gameOver(Graphics g) {
        draw.endGame(g, p1.allShips > 0 ? p1 : p2, cursorX, cursorY);

    }

    private void restartGame() {
        p1.restart();
        p2.restart();

        p1.numberOfActions += 1;
        EndGame = false;
        CreateUsers = true;
    }
    private void fixDeadShip(int x, int y){
        opponent.writeMissOrHit(x,y,false,true);
        player.writeMissOrHit(x,y,false,false);
    }
    private void checkDeadShip(int x, int y) {
        ArrayList<ArrayList<Integer>> list = opponent.searchIsKill(x, y);
        if (!list.isEmpty()) {

            List<Integer> xList =  list.get(0).stream().sorted().collect(Collectors.toList());
            List<Integer> yList =  list.get(1).stream().sorted().collect(Collectors.toList());
            System.out.println(xList);
            System.out.println(yList);

            if (xList.get(0).equals(xList.get(xList.size()-1))){
                System.out.println("working x");
                for (int i = yList.get(0)-28; i < yList.get(yList.size()-1)+29; i+=28) {
                    fixDeadShip(xList.get(0)-28,i);
                    fixDeadShip(xList.get(0)+28,i);
                }
                fixDeadShip(xList.get(0),yList.get(0)-28);
                fixDeadShip(xList.get(0),yList.get(yList.size()-1)+28);
            }
            if(yList.get(0).equals(yList.get(yList.size()-1))){
                System.out.println("working y");
                for (int i = xList.get(0)-28; i < xList.get(yList.size()-1)+29; i+=28) {
                    fixDeadShip(i,yList.get(0)-28);
                    fixDeadShip(i,yList.get(0)+28);
                }
                fixDeadShip(xList.get(0)-28,yList.get(0));
                fixDeadShip(xList.get(xList.size()-1)+28,yList.get(0));
            }

        }

    }

    public boolean checkCollision(int[] x, int[] y, int endShip) {
        if (x[0] == 0) return false;

        for (int i = 0; i < endShip; i++) if (player.checkShip(x[i], y[i])) return false;

        if (x[0] == x[endShip]) {
            for (int i = y[0] - 28; i < y[endShip] + 29; i += 28) {
                if (player.checkShip(x[0] - 28, i)) return false;
                if (player.checkShip(x[0] + 28, i)) return false;
            }
            if (player.checkShip(x[0], y[0] - 28)) return false;
            if (player.checkShip(x[0], y[endShip] + 28)) return false;
        }
        if (y[0] == y[endShip]) {
            for (int i = x[0] - 28; i < x[endShip] + 29; i += 28) {
                if (player.checkShip(i, y[0] - 28)) return false;
                if (player.checkShip(i, y[0] + 28)) return false;
            }
            if (player.checkShip(x[0] - 28, y[0])) return false;
            if (player.checkShip(x[endShip] + 28, y[0])) return false;
        }
        return true;
    }

    public void moveShip(String s, int lengthShip) {
        if (lengthShip > 0) {
            if ("left".equals(s) && shipX[0] > 40)
                for (int i = 0; i < lengthShip; i++) shipX[i] -= 28;
            if ("right".equals(s) && shipX[0] < 292 && shipX[lengthShip - 1] < 292)
                for (int i = 0; i < lengthShip; i++) shipX[i] += 28;
            if ("up".equals(s) && shipY[0] > 40 && shipY[lengthShip - 1] > 40)
                for (int i = 0; i < lengthShip; i++) shipY[i] -= 28;
            if ("down".equals(s) && shipY[lengthShip - 1] < 256 && shipY[0] < 256)
                for (int i = 0; i < lengthShip; i++) shipY[i] += 28;

            if ("enter".equals(s)) {
                player.nextShip();
                player.writeShip(shipX, shipY, 1);
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
                        if (shipY[i] > 277) for (int j = lengthShip - 1; j != -1; j--) shipY[j] = shipY[j] - 28;
                    }
                }
            }
        }
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
                    cursorX = 152;
                    cursorY = 137;
                }
            }
            if (CreateUsers) {
                if (blocking) {
                    if (key == KeyEvent.VK_LEFT) moveShip("left", player.getShip());
                    if (key == KeyEvent.VK_RIGHT) moveShip("right", player.getShip());
                    if (key == KeyEvent.VK_DOWN) moveShip("down", player.getShip());
                    if (key == KeyEvent.VK_UP) moveShip("up", player.getShip());
                    if (key == KeyEvent.VK_SPACE) moveShip("space", player.getShip());
                    if (key == KeyEvent.VK_ENTER && checkCollision(shipX, shipY, player.getShip() - 1)) {
                        blocking = false;
                        moveShip("enter", player.getShip());
                    }
                }

            }
            if (BattleGame) {
                if (key == KeyEvent.VK_SPACE) player.onlyUser = true;
                if (key == KeyEvent.VK_DOWN && cursorY < 277 && player.onlyUser) cursorY += 28;
                if (key == KeyEvent.VK_UP && cursorY > 25 && player.onlyUser) cursorY -= 28;
                if (key == KeyEvent.VK_LEFT && cursorX > 40 && player.onlyUser) cursorX -= 28;
                if (key == KeyEvent.VK_RIGHT && cursorX < 292 && player.onlyUser) cursorX += 28;
                if (key == KeyEvent.VK_ENTER && player.onlyUser && player.checkShipOpponent(cursorX, cursorY)) {
                    boolean isHit = opponent.checkShip(cursorX, cursorY);
                    if (isHit) opponent.allShips -= 1;
                    player.writeMissOrHit(cursorX, cursorY, isHit, false);
                    opponent.writeMissOrHit(cursorX, cursorY, isHit, true);
                    checkDeadShip(cursorX,cursorY);
                    if (!isHit) {
                        player.numberOfActions -= 1;
                        player.onlyUser = false;
                        opponent.numberOfActions += 1;
                    }

                }
                if (key == KeyEvent.VK_ALT) p2.allShips = 0;
            }
            if (EndGame) {
                if (key == KeyEvent.VK_DOWN && cursorY < 270) cursorY += 50;
                if (key == KeyEvent.VK_UP && cursorY > 220) cursorY -= 50;
                if (key == KeyEvent.VK_ENTER && cursorY == 220) {
                    restartGame();
                    cursorX = 152;
                    cursorY = 137;
                }
                if (key == KeyEvent.VK_ENTER && cursorY == 270) System.exit(0);
            }

        }
    }

}
