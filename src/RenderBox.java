import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public enum RenderBox {
    EMPTY("box.png"),
    SHIP("boxShip.png"),
    HIT("hit.png"),
    MISS("miss.png");

    private String fileName;
    private  Image img;
    RenderBox(String fileName) {

        ImageIcon image = new ImageIcon("resources/" + fileName);
        this.img = image.getImage();
    }

    public Image getImg(){
        return this.img;
    }
}
