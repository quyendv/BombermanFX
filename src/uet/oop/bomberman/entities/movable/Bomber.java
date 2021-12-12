package uet.oop.bomberman.entities.movable;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.movable.enemy.Enemy;
import uet.oop.bomberman.entities.still.Grass;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.AudioGame;

public class Bomber extends MovableObject {

    private boolean moving;
    private int timePutBomb;                       // sau khi đặt bom cần đợi hết tgian này mới đặt tiếp được. tránh đặt quá nhanh
    private boolean inputSpace;                    // ktra xem có nhận KeyCode.Space hay không (Space = đặt bomb)
    private int timeToReset;                       // tgian render hiệu ứng player_die
    public static int life = 5;                    // Chỉ có 5 mạng trong toàn bộ các lv, nếu chết hết sẽ reset lại = 5 và về lv1
    public static int bombLimit = 1;
    public static int flameRadius = 1;
    public static boolean wallPass = false;        // có thể đi qua wall, brick khi nhận WallPassItem
    public static boolean flamePass = false;       // miễn nhiễm với vụ nổ (flame, bomb) khi nhận FlamePassItem
    public static int speed = 3;

    public Bomber(int x, int y, Image img) {
        super(x, y, img);
        moving = false;
        timePutBomb = 0;
        inputSpace = false;
        timeToReset = 90;
    }

    @Override
    public void update() {
        animationHandle();

        moveHandle();

        putBomb();
    }

    @Override
    public boolean checkCollision(Entity e) {
        if (e instanceof Flame) {
            if (!flamePass && !this.killed) this.killed();
            return false;
        }
        if (e instanceof Enemy) {
            if (!this.killed) this.killed();
            return true;
        }
        return false;
    }

    public void controlPlayer(Scene scene) {        // chỉ nhận hướng từ bàn phím và tín hiệu đặt bom
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case RIGHT:
                    // case D:
                    this.direction = RIGHT_DIR;
                    moving = true;
                    break;
                case LEFT:
                    // case A:
                    this.direction = LEFT_DIR;
                    moving = true;
                    break;
                case UP:
                    // case W:
                    this.direction = UP_DIR;
                    moving = true;
                    break;
                case DOWN:
                    // case S:
                    this.direction = DOWN_DIR;
                    moving = true;
                    break;
                case SPACE:
                    this.inputSpace = true;
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SPACE) this.inputSpace = false;
            else moving = false;
        });
    }

    public void putBomb() {
        if (timePutBomb > 0) timePutBomb--;
        if (inputSpace && BombermanGame.bombs.size() < bombLimit && timePutBomb == 0) {
            int xBomb = ((x + 16) / Sprite.SCALED_SIZE);    // tọa độ bomb vào khoảng giữa người bomber
            int yBomb = ((y + 10) / Sprite.SCALED_SIZE);    // tọa độ bomb vào khoảng giữa người bomber

            // Nếu k cho phép đặt cùng 1 ô
            Bomb other = BombermanGame.getBombAt(xBomb, yBomb);
            if (other != null) return;

            // put bomb:

            // Chỉ cho phép đặt bomb trên cỏ (tránh đặt bomb trên brick, wall khi có wallPass)
            if (wallPass) {
                Entity e = BombermanGame.getEntityAt(xBomb, yBomb);
                if (!(e instanceof Grass)) return;
            }

            Bomb newBomb = new Bomb(xBomb, yBomb, Sprite.bomb.getFxImage());
            BombermanGame.bombs.add(newBomb);

            // sound
            AudioGame putBomb = new AudioGame(AudioGame.PUT_BOMB);
            putBomb.playAudio();

            timePutBomb = 10;
        }
    }

    public void animationHandle() {
        animate = animate + 1 % MAX_ANIMATE;
        if (killed) {
            if (timeToReset > 0) {
                img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, 90 - timeToReset, 90).getFxImage();
                timeToReset--;
            } else resetPlayer();
            return;
        }
        // else
        switch (direction) {
            case UP_DIR:
                img = Sprite.player_up.getFxImage();
                if (moving)
                    img = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, animate, 20).getFxImage();
                break;
            case RIGHT_DIR:
                img = Sprite.player_right.getFxImage();
                if (moving)
                    img = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20).getFxImage();
                break;
            case DOWN_DIR:
                img = Sprite.player_down.getFxImage();
                if (moving)
                    img = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, animate, 20).getFxImage();
                break;
            case LEFT_DIR:
                img = Sprite.player_left.getFxImage();
                if (moving)
                    img = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, animate, 20).getFxImage();
                break;
            default:
                break;
        }
    }

    /**
     * UP / DOWN: ktra va chạm ở đầu với thân bên phải
     * LEFT / RIGHT: ktra va chạm ở đầu và chân
     * Bomber: width 10-12, height 30-32
     */
    public void moveHandle() {
        if (killed) return;
        if (!moving) return;

        // update tọa độ khi đứng ở mép ô.
        if (direction == RIGHT_DIR || direction == LEFT_DIR) {
            if (y % Sprite.SCALED_SIZE >= 22) y = (y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
            else if (y % Sprite.SCALED_SIZE > 0 && y % Sprite.SCALED_SIZE <= 10)
                y = y / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE;
        } else {        // UP / DOWN
            if (x % Sprite.SCALED_SIZE >= 25) x = (x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
            else if (x % Sprite.SCALED_SIZE > 0 && x % Sprite.SCALED_SIZE <= 12)
                x = x / Sprite.SCALED_SIZE * Sprite.SCALED_SIZE + 4;
        }

        // move:

        int newX = x, newY = y;     // toạ độ trên màn hình game
        switch (direction) {
            case UP_DIR:
                newY = y - speed;
                if (x / Sprite.SCALED_SIZE == (x + 20) / Sprite.SCALED_SIZE) {
                    Entity e1 = BombermanGame.getEntityAt(x / Sprite.SCALED_SIZE, newY / Sprite.SCALED_SIZE);
                    if (e1 != null && canMove(newX, newY, e1)) y = newY;
                } else {
                    Entity e1 = BombermanGame.getEntityAt(x / Sprite.SCALED_SIZE, newY / Sprite.SCALED_SIZE);
                    Entity e2 = BombermanGame.getEntityAt((x + 20) / Sprite.SCALED_SIZE, newY / Sprite.SCALED_SIZE);
                    if (e1 != null && canMove(newX, newY, e1) && e2 != null && canMove(newX, newY, e2)) y = newY;
                }
                break;

            case RIGHT_DIR:
                newX = x + speed;
                if (y / Sprite.SCALED_SIZE == (y + 30) / Sprite.SCALED_SIZE) {
                    Entity e1 = BombermanGame.getEntityAt((newX + 20) / Sprite.SCALED_SIZE, y / Sprite.SCALED_SIZE);
                    if (e1 != null && canMove(newX, newY, e1)) x = newX;
                } else {
                    Entity e1 = BombermanGame.getEntityAt((newX + 20) / Sprite.SCALED_SIZE, y / Sprite.SCALED_SIZE);
                    Entity e2 = BombermanGame.getEntityAt((newX + 20) / Sprite.SCALED_SIZE, (y + 30) / Sprite.SCALED_SIZE);
                    if (e1 != null && canMove(newX, newY, e1) && e2 != null && canMove(newX, newY, e2)) x = newX;
                }
                break;

            case DOWN_DIR:
                newY = y + speed;
                if (x / Sprite.SCALED_SIZE == (x + 20) / Sprite.SCALED_SIZE) {
                    Entity e1 = BombermanGame.getEntityAt(x / Sprite.SCALED_SIZE, (newY + 30) / Sprite.SCALED_SIZE);
                    if (e1 != null && canMove(newX, newY, e1)) y = newY;
                } else {
                    Entity e1 = BombermanGame.getEntityAt(x / Sprite.SCALED_SIZE, (newY + 30) / Sprite.SCALED_SIZE);
                    Entity e2 = BombermanGame.getEntityAt((x + 20) / Sprite.SCALED_SIZE, (newY + 30) / Sprite.SCALED_SIZE);
                    if (e1 != null && canMove(newX, newY, e1) && e2 != null && canMove(newX, newY, e2)) y = newY;
                }
                break;

            case LEFT_DIR:
                newX = x - speed;
                if (y / Sprite.SCALED_SIZE == (y + 30) / Sprite.SCALED_SIZE) {
                    Entity e1 = BombermanGame.getEntityAt(newX / Sprite.SCALED_SIZE, y / Sprite.SCALED_SIZE);
                    if (e1 != null && canMove(newX, newY, e1)) x = newX;
                } else {
                    Entity e1 = BombermanGame.getEntityAt(newX / Sprite.SCALED_SIZE, y / Sprite.SCALED_SIZE);
                    Entity e2 = BombermanGame.getEntityAt(newX / Sprite.SCALED_SIZE, (y + 30) / Sprite.SCALED_SIZE);
                    if (e1 != null && canMove(newX, newY, e1) && e2 != null && canMove(newX, newY, e2)) x = newX;
                }
                break;

            default:
                break;
        }
    }

    public boolean canMove(int newX, int newY, Entity e) {
        if (!e.checkCollision(this)) return true;

        // Nếu ô bomber định tới (newX, newY) có thể có va chạm thì ktra xem đã va chạm chưa
        Rectangle2D bomberPos = new Rectangle2D(newX, newY, 25, 33);
        if (bomberPos.intersects(e.getX(), e.getY(), Sprite.SCALED_SIZE + 1, Sprite.SCALED_SIZE + 1)) return false;
        return true;
    }

    @Override
    public void killed() {
        this.killed = true;
        life--;

        // sound
        AudioGame player_dead = new AudioGame(AudioGame.PLAYER_DEAD);
        player_dead.playAudio();
    }

    public void resetPlayer() {
        this.setX(Sprite.SCALED_SIZE);
        this.setY(Sprite.SCALED_SIZE);
        img = Sprite.player_right.getFxImage();

        direction = RIGHT_DIR;
        moving = false;
        timePutBomb = 0;
        killed = false;
        timeToReset = 90;
        wallPass = false;
        flamePass = false;
        speed = 3;
        bombLimit = 1;
        flameRadius = 1;
        if (life <= 0) {
            life = 5;
            BombermanGame.mapLoader.setLevel(1);
            BombermanGame.createMap();
        }
    }
}
