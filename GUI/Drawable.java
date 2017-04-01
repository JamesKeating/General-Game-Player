package GUI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 * Created by siavj on 04/02/2017.
 */
public class Drawable {

    private ImageView imageView;
    private int x_coord, y_coord;

    public Drawable(int x_coord, int y_coord, String filename){
        this.x_coord = x_coord;
        this.y_coord = y_coord;
        File file = new File("D:\\FYP\\General-Game-Gameplay\\Images\\" + filename + ".png");
        Image image = new Image(file.toURI().toString());
        imageView = new ImageView(image);
    }

    public ImageView getImage(){
        return this.imageView;
    }

    public int getx(){
        return x_coord;
    }

    public int gety(){
        return y_coord;
    }
}

