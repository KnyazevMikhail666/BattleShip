import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class GameGraphics implements ImageObserver {
    private final String[] str = new String[]{"а", "б", "в", "г", "д", "е", "ж", "з", "и", "к"};
    private final String[] str1 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private Image startGameShip;
    public Image ship;
    private Image aim;
    private Image box;
    private Image hit;
    private Image miss;


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

    }
    public void startGame(Graphics g, int x, int y) {
        Font myFont = new Font("Arial", 1, 30);
        g.setFont(myFont);
        g.setColor(Color.white);

        g.drawImage(startGameShip, 280, 50, 230, 100,this);
        g.drawString("BattleShip", 320, 180);
        g.drawString("Start", 350, 280);
        g.drawString("Exit", 350, 320);
        g.drawImage(aim, x, y,this);

    }
    public void gameTable(Graphics g, int countTable , Player player) {
        int[] stepTable = new int[]{52, 15, 40};
        if (countTable>1) for (int i = 0; i < stepTable.length; i++) stepTable[i] += 430;

        g.setColor(Color.white);
        for (int i = 0; i < str.length; i++) g.drawString(str[i], stepTable[0] + (i * 28), 15);
        for (int i = 0; i < str1.length; i++) g.drawString(str1[i], stepTable[1], 44 + (i * 28));

        for (int i = 0; i < str.length; i++) {
            for (int j = 0; j < str.length; j++){
                int x = stepTable[2] + (j * 28);
                int y = 25 + (i * 28);
                Image s = player.checkShip(x,y)? ship:box;
                g.drawImage(s, stepTable[2] + (j * 28), 25 + (i * 28), this);
            }
        }
    }

    public void countShips(Graphics g, int[] numberShips) {
        for (int i = 4; i != 0; i--) {
            for (int j = i; j != 0; j--) {
                g.drawImage(ship, 455 + (j * 28), 25 + (i * 56), this);
            }
            g.drawString(Integer.toString(numberShips[i - 1]) + " " + "X", 450, 45 + (i * 56));
        }
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }
}
