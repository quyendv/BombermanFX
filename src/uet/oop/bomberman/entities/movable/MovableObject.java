package uet.oop.bomberman.entities.movable;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

/**
 * Nhóm các Entity có thể chuyển động
 * Gồm: + Bomber
 *      + Enemy
 */
public abstract class MovableObject extends Entity {
    protected static final int UP_DIR = 0;
    protected static final int RIGHT_DIR = 1;
    protected static final int DOWN_DIR = 2;
    protected static final int LEFT_DIR = 3;

    /*
                UP: 0
       LEFT: 3          RIGHT: 1
                DOWN:2
    */

    protected int direction;
    protected boolean killed;

    public MovableObject(int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
        status = EXIST;
        animate = 0;
        // super(xUnit, yUnit, img);

        direction = RIGHT_DIR;
        killed = false;
    }


    public boolean isKilled() {
        return killed;
    }

    public abstract void killed();
}
