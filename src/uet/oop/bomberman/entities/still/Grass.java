package uet.oop.bomberman.entities.still;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public class Grass extends StillObject {

    public Grass(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean checkCollision(Entity e) {
        return false;
    }

}
