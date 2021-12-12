package uet.oop.bomberman.entities.movable.enemy;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.movable.Bomber;
import uet.oop.bomberman.entities.movable.MovableObject;
import uet.oop.bomberman.entities.still.Brick;
import uet.oop.bomberman.entities.still.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map.MapLoader;
import uet.oop.bomberman.sound.AudioGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gồm: + Balloon
 * + Oneal
 * + ...
 */
public abstract class Enemy extends MovableObject {
    protected int[] xDir = {0, 1, 0, -1};
    protected int[] yDir = {-1, 0, +1, 0};
    protected Random random = new Random();
    List<Integer> randomDir = new ArrayList<Integer>();

    protected int timeToRemoved;  // tgian render hiệu ứng dead sau đó bị xóa
    protected int speed;

    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        this.speed = 1;
        for (int i = 0; i < 4; i++) {
            randomDir.add(i);
        }
        timeToRemoved = 80;
    }

    public boolean canMove(int newX, int newY) {
        Rectangle2D rect = new Rectangle2D(newX, newY, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

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


        for (Wall wall : MapLoader.walls) {
            if (rect.intersects(wall.getX(), wall.getY(), Sprite.SCALED_SIZE, Sprite.SCALED_SIZE)) {
                return false;
            }
        }

        for (Brick a : MapLoader.bricks) {
            if (rect.intersects(a.getX(), a.getY(), Sprite.SCALED_SIZE, Sprite.SCALED_SIZE)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void killed() {
        this.killed = true;
        AudioGame enemy_dead = new AudioGame(AudioGame.ENEMY_DEAD);
        enemy_dead.playAudio();
    }

    @Override
    public boolean checkCollision(Entity e) {
        if (e instanceof Bomber) {
            Bomber p = (Bomber) e;
            if (!this.killed && !p.isKilled()) p.killed();  // đâm vào xác enemy thì k chết
        }
        if (e instanceof Flame) {
            this.killed();
            return false;
        }
        return false;
    }
}
