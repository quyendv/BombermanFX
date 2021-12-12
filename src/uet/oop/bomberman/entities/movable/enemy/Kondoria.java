package uet.oop.bomberman.entities.movable.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Collections;

// speed gấp 2 enemy thường
public class Kondoria extends Enemy{

    public Kondoria(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        speed = 2;
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
                img = Sprite.movingSprite(Sprite.kondoria_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, 80 - timeToRemoved, 80).getFxImage();
                timeToRemoved--;
            } else setRemoved();
        } else {
            if (this.direction == RIGHT_DIR) {
                this.img = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, animate, 60).getFxImage();
            }
            if (this.direction == LEFT_DIR) {
                this.img = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, animate, 60).getFxImage();
            }
        }
    }
}
