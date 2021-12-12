package uet.oop.bomberman.entities.still;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.movable.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class Wall extends StillObject {

    public Wall(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean checkCollision(Entity e) {
        // if (e instanceof Minvo) return false;
        if (e instanceof Bomber) {
            if (Bomber.wallPass) {       // k đi ra ngoài map
                return x / Sprite.SCALED_SIZE == 0 || x / Sprite.SCALED_SIZE == 30
                        || y / Sprite.SCALED_SIZE == 0 || y / Sprite.SCALED_SIZE == 12;
            }
        }
        return true;
    }

}
