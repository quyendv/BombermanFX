package uet.oop.bomberman.entities.bomb;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.movable.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.AudioGame;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends Entity {
    private int timeToExplore; // tgian từ lúc đặt đến lúc nổ
    private int timeToRemoved; // tgian vụ nổ kéo dài (từ lúc nổ đến lúc bị xóa) <--> time cho Flame
    private boolean explore;
    private List<Flame> flames;

    public Bomb(int xUnit, int yUnit, Image img) {
        // super(xUnit, yUnit, img);
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
        status = EXIST;
        timeToExplore = 120;
        timeToRemoved = 30;  // 30 = 1 chu trình, 40 thêm 1 frame, 50 thêm 2 frame, ...
        explore = false;
        flames = new ArrayList<>();
    }

    @Override
    public void update() {
        clearRemovedFlames();
        if (timeToExplore > 0) timeToExplore--;
        else {
            if (!explore) {
                setExplore();  // contains checkCollision Bomber
                addFlame();
                // sound
                AudioGame explosion = new AudioGame(AudioGame.EXPLOSION);
                explosion.playAudio();
            } else {
                updateFlames();
            }

            if (timeToRemoved > 0) timeToRemoved--;
            else {
                setRemoved();      // remove in class Bomber
                // flames.clear(); // chỉ cần removed bomb
            }
        }
        animationHandle();
    }

    @Override
    public boolean checkCollision(Entity e) {
        if (e instanceof Flame) {
            // if (!this.explore) this.setExplore();
            animate += timeToExplore;
            timeToExplore = 0;
            return true;
        }
        if (e instanceof Bomber) {
            if (this.explore) {
                Bomber p = (Bomber) e;
                if (!Bomber.flamePass && !p.isKilled()) p.killed();
                return true;
            }
            int distanceX = Math.abs(this.x - e.getX());
            int distanceY = Math.abs(this.y - e.getY());
            // System.out.println(distanceX + " " + distanceY);

            // Khi đặt bom thì bomb và bomber cùng tọa độ. Sau khi bomber di chuyển 1 khoảng bằng kích thước 1 ô thì chặn lại
            if (x - e.getX() >= 20) return true; // xử lý riêng TH bomber bên trái quả bomb vì khi đó distanceX nhỏ
            return distanceX >= Sprite.SCALED_SIZE || distanceY >= Sprite.SCALED_SIZE;
        }

        // if enemy xuyên địa hình: false;
        return true;
    }

    public void animationHandle() {
        animate = animate + 1 % MAX_ANIMATE;
        if (!explore)
            img = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, 60).getFxImage();
        else {
            img = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2, animate, 30).getFxImage();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (img == null) return;
        gc.drawImage(img, x, y);
        flames.forEach(f -> f.render(gc));
    }

//    public void renderFrame() {
//        // flames.forEach(f -> f.render());
//    }

    public void updateFlames() {
        flames.forEach(Flame::update);
    }

    public void clearRemovedFlames() {
        flames.removeIf(f -> f.isRemoved());
    }

    /**
     * Tạo flame (xác định có phải flame cuối k)
     * + Trong khi tạo, nếu gặp vật cản như wall, brick, ... thì sẽ k thể render lên được (k tạo)
     * + Nếu tạo mà gặp movableObject thì kill() đối tượng đó
     */
    public void addFlame() {
        int flameRadius = Bomber.flameRadius;
        int xFlame, yFlame;

        // UP:
        for (int i = 1; i <= flameRadius; i++) {
            xFlame = x;
            yFlame = y - i * Sprite.SCALED_SIZE;
            if (i != flameRadius) {
                Entity e = BombermanGame.getEntityNotFlameAt(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE);
                Flame newFlame = new Flame(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE,
                        Sprite.explosion_vertical.getFxImage(), !Flame.LAST_FLAME, Flame.UP);

                if (e != null && e.checkCollision(newFlame)) break;
                // else: can addFlame
                flames.add(newFlame);
            } else {    // lastFlame
                Entity e = BombermanGame.getEntityNotFlameAt(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE);
                Flame newFlame = new Flame(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE,
                        Sprite.explosion_vertical_top_last.getFxImage(), Flame.LAST_FLAME, Flame.UP);

                if (e != null && e.checkCollision(newFlame)) break;
                // else: can addFlame
                flames.add(newFlame);
            }
        }

        // DOWN:
        for (int i = 1; i <= flameRadius; i++) {
            xFlame = x;
            yFlame = y + i * Sprite.SCALED_SIZE;
            if (i != flameRadius) {
                Entity e = BombermanGame.getEntityNotFlameAt(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE);
                Flame newFlame = new Flame(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE,
                        Sprite.explosion_vertical.getFxImage(), !Flame.LAST_FLAME, Flame.DOWN);

                if (e != null && e.checkCollision(newFlame)) break;
                // else: can addFlame
                flames.add(newFlame);
            } else {
                Entity e = BombermanGame.getEntityNotFlameAt(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE);
                Flame newFlame = new Flame(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE,
                        Sprite.explosion_vertical_down_last.getFxImage(), Flame.LAST_FLAME, Flame.DOWN);

                if (e != null && e.checkCollision(newFlame)) break;
                // else: can addFlame
                flames.add(newFlame);
            }
        }

        // RIGHT:
        for (int i = 1; i <= flameRadius; i++) {
            xFlame = x + i * Sprite.SCALED_SIZE;
            yFlame = y;
            if (i != flameRadius) {
                Entity e = BombermanGame.getEntityNotFlameAt(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE);
                Flame newFlame = new Flame(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE,
                        Sprite.explosion_horizontal.getFxImage(), !Flame.LAST_FLAME, Flame.RIGHT);

                if (e != null && e.checkCollision(newFlame)) break;
                // else: can addFlame
                flames.add(newFlame);
            } else {
                Entity e = BombermanGame.getEntityNotFlameAt(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE);
                Flame newFlame = new Flame(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE,
                        Sprite.explosion_horizontal_right_last.getFxImage(), Flame.LAST_FLAME, Flame.RIGHT);

                if (e != null && e.checkCollision(newFlame)) break;
                // else: can addFlame
                flames.add(newFlame);
            }
        }

        // LEFT:
        for (int i = 1; i <= flameRadius; i++) {
            xFlame = x - i * Sprite.SCALED_SIZE;
            yFlame = y;
            if (i != flameRadius) {
                Entity e = BombermanGame.getEntityNotFlameAt(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE);
                Flame newFlame = new Flame(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE,
                        Sprite.explosion_horizontal.getFxImage(), !Flame.LAST_FLAME, Flame.LEFT);

                if (e != null && e.checkCollision(newFlame)) break;
                // else: can addFlame
                flames.add(newFlame);
            } else {
                Entity e = BombermanGame.getEntityNotFlameAt(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE);
                Flame newFlame = new Flame(xFlame / Sprite.SCALED_SIZE, yFlame / Sprite.SCALED_SIZE,
                        Sprite.explosion_horizontal_left_last.getFxImage(), Flame.LAST_FLAME, Flame.LEFT);

                if (e != null && e.checkCollision(newFlame)) break;
                // else: can addFlame
                flames.add(newFlame);
            }
        }
    }

    public void setExplore() {
        // Tâm của bomb k phải flame nhưng vẫn tiêu diệt đc bomber, enemy và bomb khác
        this.explore = true;

        // if bomber
        collideExploreBombWithBomber(this);

        // if Enemy:... // k cần vì enemy k đi vào giữa bomb được
    }

    public void collideExploreBombWithBomber(Entity e) {  // bombExplore <-> Bomber
        Bomber p = BombermanGame.getBomber();
        Rectangle2D currentPos = new Rectangle2D(e.getX(), e.getY(), Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
        Rectangle2D bomberPos = new Rectangle2D(p.getX(), p.getY(), 20, 32);
        if (currentPos.intersects(bomberPos)) {
            if (!Bomber.flamePass && !p.isKilled()) p.killed();
        }
    }

    public int getTimeToExplore() {
        return timeToExplore;
    }

    public void setTimeToExplore(int timeToExplore) {
        this.timeToExplore = timeToExplore;
    }

    public int getTimeToRemoved() {
        return timeToRemoved;
    }

    public void setTimeToRemoved(int timeToRemoved) {
        this.timeToRemoved = timeToRemoved;
    }

    public boolean isExplore() {
        return explore;
    }

    public void setExplore(boolean explore) {
        this.explore = explore;
    }

    public List<Flame> getFlames() {
        return flames;
    }

    public void setFlames(List<Flame> flames) {
        this.flames = flames;
    }
}
