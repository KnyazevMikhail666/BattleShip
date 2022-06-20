import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class GameField extends JPanel implements ActionListener {
    private Timer timer;
    private String[] str = new String[]{"а", "б", "в", "г", "д", "е", "ж", "з", "и", "к"};
    private String[] str1 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private Image startGameShip;
    private Image ship;
    private Image aim;
    private Image box;
    private Image hit;
    private Image miss;
    final private int PLAYERS_FIELD = 320;
    final private int MENU_FIELD = 150;
    public int cursorX = 310;
    public int cursorY = 250;
    private boolean inUser1 = false;
    private boolean inUser2 = false;

    private boolean StartGame = true;
    private boolean createUsers = false;
    private boolean battleGame = false;

    public GameField() {
        setBackground(Color.BLACK);
        loadImages();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
        timer = new Timer(250, this);
        timer.start();


    }

    public void initGame() {

    }

    public void move() {
    }

    private void startGame(Graphics g) {
        Font myFont = new Font("Arial", 1, 30);
        g.setFont(myFont);
        Graphics2D d = (Graphics2D) g;
        g.setColor(Color.white);

        g.drawImage(startGameShip, 280, 50, 230, 100, this);
        g.drawString("BattleShip", 320, 180);
        g.drawString("Start", 350, 280);
        g.drawString("Exit", 350, 320);
        g.drawImage(aim, cursorX, cursorY, this);

    }

    private void drawGameTable(Graphics g, boolean user) {
        int[] x = new int[]{52, 15, 40};
        if (user) for (int i = 0; i < x.length; i++) x[i] += 430;
        Graphics2D d = (Graphics2D) g;
        g.setColor(Color.white);
        for (int i = 0; i < str.length; i++) g.drawString(str[i], x[0] + (i * 28), 15);
        for (int i = 0; i < str1.length; i++) g.drawString(str1[i], x[1], 44 + (i * 28));

        for (int i = 0; i < str.length; i++) {
            for (int j = 0; j < str.length; j++) g.drawImage(box, x[2] + (j * 28), 25 + (i * 28), this);
        }
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (StartGame) startGame(g);

        else {
            drawGameTable(g, true);
            drawGameTable(g, false);
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
                if (key == KeyEvent.VK_ENTER && cursorY == 250) StartGame = false;
            }
        }
    }

}
