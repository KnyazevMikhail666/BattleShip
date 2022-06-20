import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow(){
        setTitle("Battle Ship");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(790,380);
        setLocation(400,400);
        add(new GameField());
        setVisible(true);
    }

    public static void main(String[] args) {

        MainWindow mw = new MainWindow();
    }
}
