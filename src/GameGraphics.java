import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class GameGraphics implements ImageObserver {
    private final String[] str = new String[]{"а", "б", "в", "г", "д", "е", "ж", "з", "и", "к"};
    private final String[] str1 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private final Font myFont = new Font("Arial", 10, 30);
    private Image startGameShip;
    public Image ship;
    private Image aim;
    private Image box;
    private Image hit;
    private Image miss;
    private ArrayList<Image> images = new ArrayList<>();


    public GameGraphics(){
        loadImages();
    }

    public void loadImages() {
        ImageIcon iibox = new ImageIcon("box.png");
        ImageIcon iiship = new ImageIcon("boxShip.png");
        ImageIcon iihit = new ImageIcon("hit.png");
        ImageIcon iiaim = new ImageIcon("aim.png");
        ImageIcon iimiss = new ImageIcon("miss.png");
        ImageIcon iiSGS = new ImageIcon("StartGameShip.png");
        box = iibox.getImage();
        ship = iiship.getImage();
        hit = iihit.getImage();
        aim = iiaim.getImage();
        miss = iimiss.getImage();
        startGameShip = iiSGS.getImage();
        images.add(box);
        images.add(ship);
        images.add(hit);
        images.add(miss);
    }
    public void startGame(Graphics g, int x, int y) {
        g.setFont(myFont);
        g.setColor(Color.white);

        g.drawImage(startGameShip, 280, 50, 230, 100,this);
        g.drawString("BattleShip", 320, 180);
        g.drawString("Start", 350, 280);
        g.drawString("Exit", 350, 320);
        g.drawImage(aim, x, y,this);

    }
    public void drawAim(Graphics g, int x , int y){
        g.drawImage(aim, x,y,this);
    }
    private void renderCharTable(Graphics g, int x , int y){
        g.setColor(Color.white);
        for (int i = 0; i < str.length; i++) g.drawString(str[i], x + (i * 28), 15);
        for (int i = 0; i < str1.length; i++) g.drawString(str1[i], y, 44 + (i * 28));
    }
    private void renderSharps(Graphics g, int step, Player player, boolean isOpponent){
        Image s;
        for (int i = 0; i < str.length; i++) {
            for (int j = 0; j < str.length; j++){
                int x = 40 + (j * 28);
                int y = 25 + (i * 28);
                if(isOpponent) s = images.get(player.renderHitAndMiss(x,y,player.opponentMap));
                else  s = images.get(player.renderHitAndMiss(x,y,player.playerMap));

                g.drawImage(s, step + (j * 28), 25 + (i * 28), this);
            }
        }
    }
    public void introPlayer(Graphics g, Player player){
        g.setFont(myFont);
        g.setColor(Color.white);
        g.drawString("It's " + player.name+"'s turn", 300,200 );
        g.setColor(Color.GRAY);
        g.drawString("Press the space bar to continue", 60,300 );
    }
    public void endGame(Graphics g, Player winner,int aimX,int aimY){
        gameTable(g,1,winner,false);
        g.setFont(myFont);
        g.setColor(Color.GREEN);
        g.drawString(winner.name+" won!!!", 450,50);
        g.setColor(Color.white);
        g.drawString("restart", 450,250);
        g.drawString("exit", 450,300);
        drawAim(g,aimX,aimY);

    }
    public void info(Graphics g, Player opponent){
        g.setColor(Color.white);
        g.drawString("Your Ships", 620, 330);
        g.drawString("number of active decks of vessels  "+opponent.name+"   :"+opponent.allShips, 60,330);
    }
    public void gameTable(Graphics g, int countTable , Player player, boolean isOpponent) {
        int[] stepTable = new int[]{52, 15, 40};
        if (countTable>1) for (int i = 0; i < stepTable.length; i++) stepTable[i] += 430;

        renderCharTable(g,stepTable[0], stepTable[1]);
        renderSharps(g,stepTable[2],player,isOpponent);
    }

    public void countShips(Graphics g, Player player) {
        for (int i = 4; i != 0; i--) {
            for (int j = i; j != 0; j--) {
                g.drawImage(ship, 455 + (j * 28), 25 + (i * 56), this);
            }
            g.drawString(Integer.toString(player.numberShips[i - 1]) + " " + "X", 450, 45 + (i * 56));
        }
        g.setFont(myFont);
        g.drawString(player.name , 450 , 50);
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }
}
