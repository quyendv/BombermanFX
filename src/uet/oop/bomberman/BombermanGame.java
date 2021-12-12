package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.movable.Bomber;
import uet.oop.bomberman.entities.movable.enemy.Enemy;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map.MapLoader;
import uet.oop.bomberman.sound.AudioGame;

import java.util.ArrayList;
import java.util.List;

public class BombermanGame extends Application {

    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;

    private GraphicsContext gc;
    private Canvas canvas;

    public static List<Bomb> bombs = new ArrayList<>();
    public static List<Enemy> enemies = new ArrayList<>();
    public static Entity[][] stillObject;
    public static Bomber player;
    public static MapLoader mapLoader = new MapLoader();
    public static String path = System.getProperty("user.dir") + "/res/";

    public static void main(String[] args) {
        AudioGame backgroundAudio = new AudioGame(AudioGame.BACKGROUND);
        backgroundAudio.playLoopAudio();
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        createMap();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                player.controlPlayer(scene);
                update();
                render();
                sleep(10);
            }
        };
        timer.start();
    }

    public static void createMap() {
        mapLoader.loadLevel();
        mapLoader.createEntities();
    }

    public void update() {
        clearRemovedEnemy();
        clearRemovedBombs();

        // update all
        for (Entity[] entities : stillObject) {
            for (Entity e : entities) e.update();
        }
        bombs.forEach(Bomb::update);
        enemies.forEach(Entity::update);
        player.update();
    }

    // renderOrder: background(stillObject) -> Bomb -> Character (tránh ảnh khác che mất nhân vật)
    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Entity[] entities : stillObject) {
            for (Entity e : entities) e.render(gc);
        }
        bombs.forEach(g -> g.render(gc));
        enemies.forEach(g -> g.render(gc));
        player.render(gc);
    }

    public static void nextLevel() {
        // sound
        AudioGame nextLevel = new AudioGame(AudioGame.NEXT_LEVEL);
        nextLevel.playAudio();

        // create next map
        mapLoader.setLevel(Math.min(mapLoader.getLevel() + 1, 5)); // giới hạn lv5
        createMap();
    }

    // Các hàm tìm phần tử trên màn hình với tọa độ x, y (tọa độ fileMap). Không bao gồm Bomber
    public static Entity getEntityAt(int x, int y) {
        Entity result = getFlameAt(x, y);
        if (result != null) return result;

        result = getBombAt(x, y);
        if (result != null) return result;

        result = getEnemyAt(x, y);
        if (result != null) return result;

        result = getStillObjectAt(x, y);
        if (result != null) return result;

        return null;        // Note: result luôn != null trừ khi truyền vào x, y ngoài map
    }

    public static Entity getEntityNotFlameAt(int x, int y) {
        Entity result = getBombAt(x, y);
        if (result != null) return result;

        result = getEnemyAt(x, y);
        if (result != null) return result;

        if (player.getX() / Sprite.SCALED_SIZE == x && player.getY() / Sprite.SCALED_SIZE == y) return player;

        result = getStillObjectAt(x, y);
        if (result != null) return result;

        return null;
    }

    public static Bomb getBombAt(int x, int y) {
        for (Bomb b : bombs) {
            if (b.getX() / Sprite.SCALED_SIZE == x && b.getY() / Sprite.SCALED_SIZE == y) return b;
        }
        return null;
    }

    public static Flame getFlameAt(int x, int y) {
        for (Bomb b : bombs) {
            for (Flame f : b.getFlames()) {
                if (f.getX() / Sprite.SCALED_SIZE == x && f.getY() / Sprite.SCALED_SIZE == y) return f;
            }
        }
        return null;
    }

    public static Enemy getEnemyAt(int x, int y) {
        for (Enemy e : enemies) {
            if (e.getX() / Sprite.SCALED_SIZE == x && e.getY() / Sprite.SCALED_SIZE == y) return e;
        }
        return null;
    }

    public static Entity getStillObjectAt(int x, int y) {
        if (x < 0 || y < 0 || x >= mapLoader.getColumns() || y >= mapLoader.getRows()) return null;
        Entity e = stillObject[x][y];
         if (e instanceof LayeredEntity) {
             return ((LayeredEntity)e).lastEntity();
         }
        return e;
    }

    public static Bomber getBomber() {
        return player;
    }

    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearRemovedEnemy() {
        enemies.removeIf(e -> e.isRemoved());
    }
    public void clearRemovedBombs() {
        bombs.removeIf(b -> b.isRemoved());
    }
}
