import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameView extends JPanel implements ActionListener {
    private final GameGraphics draw = new GameGraphics();
    private final GameController controller = new GameController();
    private final ModelPlayer p1 = new ModelPlayer("Player 1", 1);
    private final ModelPlayer p2 = new ModelPlayer("Player 2", 0);
    public ModelPlayer player = p1; //Active player
    public ModelPlayer opponent = p2; // His opponent

    public int cursorX = 310;
    public int cursorY = 250;
    public int[] shipX = new int[4];
    public int[] shipY = new int[4];

    private boolean startGame = true;
    private boolean createUsers = false;
    private boolean battleGame = false;
    private boolean endGame = false;
    private boolean blocking = true;


    public GameView() {
        setBackground(Color.BLACK);
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
        Timer timer = new Timer(250, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (startGame) start(g);
        if (createUsers) playerFleetDeployment(g);
        if (battleGame) battleGame(g);
        if(endGame) gameOver(g);
    }

    private void start(Graphics g) {
        draw.startGame(g, cursorX, cursorY);
    }

    private void playerFleetDeployment(Graphics g) {
        player = !p1.readyToPlay ? p1 : p2;
        if (p1.readyToPlay && p2.readyToPlay) {
            createUsers = false;
            battleGame = true;
        }
        int ship = controller.getPlayerShip(player);

        if (ship == 0) player.readyToPlay = true;

        draw.fleetDeployment(g, player);
        if (shipX[0] == 0 && shipY[0] == 0) updateShip(controller.newShip(shipX, shipY, ship));
        draw.drawShip(g, shipX, shipY, ship);

        blocking = true;
    }

    private void battleGame(Graphics g) {
        player = p1.numberOfActions > 0 ? p1 : p2;
        opponent = p1.numberOfActions == 0 ? p1 : p2;
        if (p1.allShips == 0 || p2.allShips == 0) {
            cursorX = 400;
            cursorY = 220;
            battleGame = false;
            endGame = true;
        }
        if (!player.onlyUser) draw.standbyScreen(g, player);
        else {
            draw.paintGameTable(g, 1, player, true);
            draw.paintGameTable(g, 2, player, false);
            draw.infoInGame(g, opponent);
            draw.drawAim(g, cursorX, cursorY);
        }
    }
    private void gameOver(Graphics g) {
        draw.endGame(g, p1.allShips > 0 ? p1 : p2, cursorX, cursorY);

    }
    private void restartGame() {
        controller.restartPLayers(new ModelPlayer[]{p1,p2});
        p1.numberOfActions += 1;
        endGame = false;
        createUsers = true;
    }

    private void updateShip(GameController.Pair<int[]> pair) {
        for (int i = 0; i < shipX.length; i++) {
            shipX[i] = pair.getX()[i];
            shipY[i] = pair.getY()[i];
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

            if (startGame) {
                if (key == KeyEvent.VK_DOWN && cursorY < 290) cursorY = controller.moveAimInMenu(cursorY, "down");
                if (key == KeyEvent.VK_UP && cursorY > 250) cursorY = controller.moveAimInMenu(cursorY, "up");
                if (key == KeyEvent.VK_ENTER && cursorY == 290) System.exit(0);
                if (key == KeyEvent.VK_ENTER && cursorY == 250) {
                    startGame = false;
                    createUsers = true;
                    cursorX = 152;
                    cursorY = 137;
                }
            }
            if (createUsers) {
                if (blocking) {
                    if (key == KeyEvent.VK_LEFT) updateShip(controller.moveShips("left", shipX, shipY, player));
                    if (key == KeyEvent.VK_RIGHT) updateShip(controller.moveShips("right", shipX, shipY, player));
                    if (key == KeyEvent.VK_DOWN) updateShip(controller.moveShips("down", shipX, shipY, player));
                    if (key == KeyEvent.VK_UP) updateShip(controller.moveShips("up", shipX, shipY, player));
                    if (key == KeyEvent.VK_SPACE) updateShip(controller.moveShips("space", shipX, shipY, player));
                    if (key == KeyEvent.VK_ENTER && controller.checkCollision(shipX, shipY, player)) {
                        blocking = false;
                        updateShip(controller.moveShips("enter", shipX, shipY, player));
                    }
                }
            }
            if (battleGame) {
                if(key == KeyEvent.VK_ALT) p1.allShips =0;
                if (key == KeyEvent.VK_SPACE) player.onlyUser = true;
                if (key == KeyEvent.VK_DOWN && cursorY < 277 && player.onlyUser) cursorY += 28;
                if (key == KeyEvent.VK_UP && cursorY > 25 && player.onlyUser) cursorY -= 28;
                if (key == KeyEvent.VK_LEFT && cursorX > 40 && player.onlyUser) cursorX -= 28;
                if (key == KeyEvent.VK_RIGHT && cursorX < 292 && player.onlyUser) cursorX += 28;
                if (key == KeyEvent.VK_ENTER
                        && player.onlyUser
                        && controller.checkEmptyBoxInTheOpponentMAp(cursorX, cursorY, player)) {
                    boolean isHit = controller.checkShipInThePlayerMap(cursorX, cursorY, opponent);
                    if (isHit) opponent.allShips -= 1;
                    controller.writeHitOrMissPlayers(cursorX, cursorY, isHit, player, opponent);
                    controller.checkDeadShip(cursorX,cursorY,player,opponent);
                    if (!isHit) {
                        player.numberOfActions -= 1;
                        player.onlyUser = false;
                        opponent.numberOfActions += 1;
                    }
                }
            }
            if (endGame) {
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
