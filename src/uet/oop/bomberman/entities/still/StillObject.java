package uet.oop.bomberman.entities.still;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

/**
 * Nhóm các entity bất động
 * Gồm: + Item
 *      + Brick, Grass, Wall, ...
 */
public abstract class StillObject extends Entity {
    public StillObject(int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
        status = EXIST;
        animate = 0;
        // super(xUnit, yUnit, img);
    }

}
