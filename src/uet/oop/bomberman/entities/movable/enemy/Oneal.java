package uet.oop.bomberman.entities.movable.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.movable.Bomber;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Collections;

// Có khả năng đuổi theo trong phạm vi (nếu cùng hàng || cột), speed thay đổi nhanh chậm
public class Oneal extends Enemy {
    public Oneal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public boolean onRadius(Bomber b, Enemy e, int r) {
        double dist = Math.sqrt(Math.pow((b.getX() - e.getX()), 2) + Math.pow((b.getY() - e.getY()), 2));
        return (dist < r);
    }

    @Override
    public void update() {
        animationHandle();
        moveHandle();
    }

    public void moveHandle() {
        if (killed) return;
        // thêm random speed sau
        if (onRadius(BombermanGame.getBomber(), this, 150)) {
            speed = 2;  // tăng tốc
            Bomber p = BombermanGame.getBomber();
            if (p.getX() / Sprite.SCALED_SIZE < this.getX() / Sprite.SCALED_SIZE
                    && p.getY() / Sprite.SCALED_SIZE == this.getY() / Sprite.SCALED_SIZE && canMove(x - speed, y)) {
                direction = LEFT_DIR;
            } else if (p.getX() / Sprite.SCALED_SIZE > this.getX() / Sprite.SCALED_SIZE
                    && p.getY() / Sprite.SCALED_SIZE == this.getY() / Sprite.SCALED_SIZE && canMove(x + speed, y)) {
                direction = RIGHT_DIR;
            }
            if (p.getY() / Sprite.SCALED_SIZE > this.getY() / Sprite.SCALED_SIZE
                    && p.getX() / Sprite.SCALED_SIZE == this.getX() / Sprite.SCALED_SIZE && canMove(x, y + speed)) {
                direction = DOWN_DIR;
            } else if (p.getY() / Sprite.SCALED_SIZE < this.getY() / Sprite.SCALED_SIZE
                    && p.getX() / Sprite.SCALED_SIZE == this.getX() / Sprite.SCALED_SIZE && canMove(x, y - speed)) {
                direction = UP_DIR;
            }
        } else {
            Collections.shuffle(randomDir);
            if (this.x % 32 == 0 && this.y % 32 == 0) {
                direction = random.nextInt(4);
                speed = random.nextInt(3);  // [0, 2]
            }
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
                img = Sprite.movingSprite(Sprite.oneal_dead, Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, 80 - timeToRemoved, 80).getFxImage();
                timeToRemoved--;
            } else setRemoved();
        } else {
            if (this.direction == RIGHT_DIR) {
                this.img = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, 60).getFxImage();
            }
            if (this.direction == LEFT_DIR) {
                this.img = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, animate, 60).getFxImage();
            }
        }
    }
}
