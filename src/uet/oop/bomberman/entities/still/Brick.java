package uet.oop.bomberman.entities.still;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.movable.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map.MapLoader;

public class Brick extends StillObject {

    private boolean destroyed; // true khi bị Flame collide, != status Removed (destroyed + hiệu ứng -> removed)
    private int timeToRemove;  // giống bomb

    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        animate = 0;
        destroyed = false;
        timeToRemove = 30;
    }

    @Override
    public void update() {
        animationHandle();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (img == null) return;
        gc.drawImage(img, x, y);
    }

    public void animationHandle() {
        if (destroyed) {
            animate = animate + 1 % MAX_ANIMATE;
            if (timeToRemove > 0) {
                timeToRemove--;
                img = Sprite.movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1, Sprite.brick_exploded2, animate, 31).getFxImage();
            } else setRemoved(); // remove ở class LayeredEntity
        }
        // else img = Sprite.brick.getFxImage();
    }

    @Override
    public boolean checkCollision(Entity e) {
        if (e instanceof Flame) {
            setDestroyed();
            MapLoader.bricks.removeIf(brick -> brick.getX() == x && brick.getY() == y);
            return true;
        }

        // if (e instanceof Minvo) return false;
        if (e instanceof Bomber) {
            return !Bomber.wallPass;
        }
        return true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed() {
        this.destroyed = true;
    }
}
