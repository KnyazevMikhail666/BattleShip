import javax.swing.*;
import java.awt.*;

public enum GameImg {
    START("StartGameShip.png"),
    AIM("aim.png");
    private String fileName;
    private Image img;
    GameImg(String fileName) {
        ImageIcon imageName = new ImageIcon("resources/" + fileName);
        this.img = imageName.getImage();
    }

    public Image getImg() {
        return this.img;
    }
}
