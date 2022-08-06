import java.awt.*;
import java.awt.image.ImageObserver;

public class GameGraphics implements ImageObserver {
    private final String[] str = new String[]{"а", "б", "в", "г", "д", "е", "ж", "з", "и", "к"};
    private final String[] str1 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private final Font myFont = new Font("Arial", 10, 30);
    private final Image startGameShip = GameImg.START.getImg();
    private final Image aim = GameImg.AIM.getImg();

    public void startGame(Graphics g, int x, int y) {
        g.setFont(myFont);
        g.setColor(Color.white);
        g.drawImage(startGameShip, 280, 50, 230, 100, this);
        g.drawString("BattleShip", 320, 180);
        g.drawString("Start", 350, 280);
        g.drawString("Exit", 350, 320);
        g.drawImage(aim, x, y, this);

    }

    public void drawAim(Graphics g, int x, int y) {
        g.drawImage(aim, x, y, this);
    }

    private void paintCharTable(Graphics g, int x, int y, boolean isOpponent) {
        if (isOpponent) {
            x += 430;
            y += 430;
        }
        g.setColor(Color.white);
        for (int i = 0; i < str.length; i++) g.drawString(str[i], x + (i * 28), 15);
        for (int i = 0; i < str1.length; i++) g.drawString(str1[i], y, 44 + (i * 28));
    }

    private void renderTable(Graphics g, int step, ModelPlayer player, boolean isOpponent) {
        Image s;
        for (int i = 0; i < str.length; i++) {
            for (int j = 0; j < str.length; j++) {
                int x = 40 + (j * 28);
                int y = 25 + (i * 28);
                if(isOpponent) s = RenderBox.values()[player.mapToOpponent.get(x).get(y)].getImg();
                else s =  RenderBox.values()[player.mapToPlayer.get(x).get(y)].getImg();
                g.drawImage(s, step + (j * 28), 25 + (i * 28), this);
            }
        }
    }

    public void standbyScreen(Graphics g, ModelPlayer player) {
        g.setFont(myFont);
        g.setColor(Color.white);
        g.drawString("It's " + player.name + "'s turn", 300, 200);
        g.setColor(Color.GRAY);
        g.drawString("Press the space bar to continue", 80, 300);
    }

    public void endGame(Graphics g, ModelPlayer winner, int aimX, int aimY) {
        paintGameTable(g, 1, winner, false);
        g.setFont(myFont);
        g.setColor(Color.GREEN);
        g.drawString(winner.name + " won!!!", 450, 50);
        g.setColor(Color.white);
        g.drawString("restart", 450, 250);
        g.drawString("exit", 450, 300);
        drawAim(g, aimX, aimY);

    }

    public void infoInGame(Graphics g, ModelPlayer opponent) {
        g.setColor(Color.white);
        g.drawString("Your Ships", 620, 330);
        g.drawString("number of active decks of vessels  " + opponent.name + "   :" + opponent.allShips, 60, 330);
    }

    public void paintGameTable(Graphics g, int countTable, ModelPlayer player, boolean isOpponent) {
        countTable = countTable > 1 ? 470 : 40;

        paintCharTable(g, 52, 15, isOpponent);
        renderTable(g, countTable, player, isOpponent);
    }

    public void fleetDeployment(Graphics g , ModelPlayer player){
        paintCharTable(g, 52,15, false);
        renderTable(g,40,player, false);
        showCountShips(g,player);
    }
    private void showCountShips(Graphics g, ModelPlayer player) {
        for (int i = 4; i != 0; i--) {
            for (int j = i; j != 0; j--) {
                g.drawImage(RenderBox.SHIP.getImg(), 455 + (j * 28), 25 + (i * 56), this);
            }
            g.drawString(Integer.toString(player.numberShips[i - 1]) + " " + "X", 450, 45 + (i * 56));
        }
        g.setFont(myFont);
        g.drawString(player.name, 450, 50);
    }

    public void drawShip(Graphics g, int[]x,int[]y, int length){
        for (int i = 0; i < length; i++) {
            g.drawImage(RenderBox.SHIP.getImg(), x[i],y[i],this);
        }
    }


    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }
}
