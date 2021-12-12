package uet.oop.bomberman.entities.bomb;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.movable.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class Flame extends Entity {
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final boolean LAST_FLAME = true;

    private boolean isLast;
    private int direction;

    public Flame(int xUnit, int yUnit, Image img, boolean isLast, int direction) {
        // super(xUnit, yUnit, img);
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
        status = EXIST;
        this.isLast = isLast;
        this.direction = direction;
    }

    @Override
    public void update() {
        animationHandle();
    }

    @Override
    public boolean checkCollision(Entity e) {
        if (e instanceof Bomber) {
            Bomber p = (Bomber) e;
            if (!Bomber.wallPass && !p.isKilled()) p.killed();
            // System.out.println("collide flame & bomber");
            return false;
        }
        // if e instanceof Enemy ...
        // if e : Flame return false;
        return false;
    }

    public void animationHandle() {
        animate = animate + 1 % MAX_ANIMATE;
        if (!isLast()) {
            if (direction == UP || direction == DOWN) {
                img = Sprite.movingSprite(Sprite.explosion_vertical, Sprite.explosion_vertical1, Sprite.explosion_vertical2, animate, 30).getFxImage();
            } else {
                img = Sprite.movingSprite(Sprite.explosion_horizontal, Sprite.explosion_horizontal1, Sprite.explosion_horizontal2, animate, 30).getFxImage();
            }
        } else {
            switch (direction) {
                case UP:
                    img = Sprite.movingSprite(Sprite.explosion_vertical_top_last, Sprite.explosion_vertical_top_last1, Sprite.explosion_vertical_top_last2, animate, 30).getFxImage();
                    break;
                case DOWN:
                    img = Sprite.movingSprite(Sprite.explosion_vertical_down_last, Sprite.explosion_vertical_down_last1, Sprite.explosion_vertical_down_last2, animate, 30).getFxImage();
                    break;
                case LEFT:
                    img = Sprite.movingSprite(Sprite.explosion_horizontal_left_last, Sprite.explosion_horizontal_left_last1, Sprite.explosion_horizontal_left_last2, animate, 30).getFxImage();
                    break;
                case RIGHT:
                    img = Sprite.movingSprite(Sprite.explosion_horizontal_right_last, Sprite.explosion_horizontal_right_last1, Sprite.explosion_horizontal_right_last2, animate, 30).getFxImage();
                    break;
                default:
                    break;
            }
        }
    }

    public boolean isLast() {
        return isLast == LAST_FLAME;
    }

    public void setLastFlame() {
        isLast = true;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
