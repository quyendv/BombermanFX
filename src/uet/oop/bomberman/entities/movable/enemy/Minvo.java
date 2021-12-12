package uet.oop.bomberman.entities.movable.enemy;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.movable.Bomber;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Collections;

// Có thể vượt wall, brick, speed chậm (== balloon)
public class Minvo extends Enemy {

    public Minvo(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        animationHandle();
        moveHandle();
    }

    public void moveHandle() {
        if (killed) return;
        Collections.shuffle(randomDir);
        if (this.x % 32 == 0 && this.y % 32 == 0) {
            this.direction = random.nextInt(4);
        }

        int newX = this.x + xDir[this.direction] * this.speed;
        int newY = this.y + yDir[this.direction] * this.speed;

        if (!canMove(newX, newY)) {
            for (int id = 0; id < 4; ++id) {
                int i = randomDir.get(id);
                newX = this.x + xDir[i] * this.speed;
                newY = this.y + yDir[i] * this.speed;
                if (canMove(newX, newY)) {
                    this.direction = i;
                    break;
                }
            }
            if (!canMove(newX, newY)) {
                newX = this.x;
                newY = this.y;
            }
        }

        this.setX(newX);
        this.setY(newY);
    }

    public void animationHandle() {
        animate = animate + 1 % MAX_ANIMATE;
        if (killed) {
            if (timeToRemoved > 0) {
                img = Sprite.movingSprite(Sprite.minvo_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, 80 - timeToRemoved, 80).getFxImage();
                timeToRemoved--;
            } else setRemoved();
        } else {
            if (this.direction == RIGHT_DIR) {
                this.img = Sprite.movingSprite(Sprite.minvo_right1, Sprite.minvo_right2, Sprite.minvo_right3, animate, 60).getFxImage();
            }
            if (this.direction == LEFT_DIR) {
                this.img = Sprite.movingSprite(Sprite.minvo_left1, Sprite.minvo_left2, Sprite.minvo_left3, animate, 60).getFxImage();
            }
        }
    }

    @Override
    public boolean canMove(int newX, int newY) {
        Rectangle2D rect = new Rectangle2D(newX, newY, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

        // check đi ra ngoài map:
        if (rect.intersects(0, 0, Sprite.SCALED_SIZE * 31, Sprite.SCALED_SIZE)                                  // top
                || rect.intersects(0, 0, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE * 13)                           // left
                || rect.intersects(0, 12 * Sprite.SCALED_SIZE, Sprite.SCALED_SIZE * 31, Sprite.SCALED_SIZE)     // bottom
                || rect.intersects(30 * Sprite.SCALED_SIZE, 0, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE * 13)) {  // right
            return false;
        }

        // collide Bomber
        Bomber p = BombermanGame.getBomber();
        if (rect.intersects(p.getX(), p.getY(), 24, 32)) return !p.checkCollision(this);

        // collide Bomb || Flame
        for (Bomb bomb : BombermanGame.bombs) {
            for (Flame flame : bomb.getFlames()) {
                if (rect.intersects(flame.getX(), flame.getY(), Sprite.SCALED_SIZE, Sprite.SCALED_SIZE)) {
                    this.killed();
                    return true;
                }
            }
            if (rect.intersects(bomb.getX(), bomb.getY(), Sprite.SCALED_SIZE, Sprite.SCALED_SIZE)) {
                if (bomb.isExplore()) {
                    this.killed();
                    return true;
                } else return false;
            }
        }
        return true;
    }
}
