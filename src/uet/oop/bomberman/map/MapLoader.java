package uet.oop.bomberman.map;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.movable.Bomber;
import uet.oop.bomberman.entities.movable.enemy.Balloon;
import uet.oop.bomberman.entities.movable.enemy.Kondoria;
import uet.oop.bomberman.entities.movable.enemy.Minvo;
import uet.oop.bomberman.entities.movable.enemy.Oneal;
import uet.oop.bomberman.entities.still.Brick;
import uet.oop.bomberman.entities.still.Grass;
import uet.oop.bomberman.entities.still.Item;
import uet.oop.bomberman.entities.still.Wall;
import uet.oop.bomberman.graphics.Sprite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {
    private int level;
    private int columns;
    private int rows;
    private char[][] map;

    public static List<Wall> walls = new ArrayList<>();
    public static List<Brick> bricks = new ArrayList<>();

    public MapLoader() {
        level = 1;
        columns = 0;
        rows = 0;
        map = null;
    }

    public void loadLevel() {
        // ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // URL url = loader.getResource("./levels/Level"+level+".txt");
        // String fileName = url.getPath();

        String fileName = BombermanGame.path + "levels/Level" + this.level + ".txt";
        try {
            BufferedReader bf = new BufferedReader(new FileReader(fileName));

            String line = bf.readLine();

            String[] data = line.split(" ", 3);

            // level = Integer.parseInt(data[0]);
            rows = Integer.parseInt(data[1]);
            columns = Integer.parseInt(data[2]);

            map = new char[rows][columns];

            for (int i = 0; i < rows; i++) {
                line = bf.readLine();
                for (int j = 0; j < columns; j++) {
                    map[i][j] = line.charAt(j);
                }
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
            // throw new IOException("Error! Can't load file " + fileName, e);
        }
    }

    public void createEntities() {
        clearMap();
        BombermanGame.stillObject = new Entity[columns][rows]; // tọa độ Entity(x, y) <-> arr[column x, row y]
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                switch (map[i][j]) {
                    // Movable:
                    case 'p':
                        BombermanGame.player = new Bomber(j, i, Sprite.player_right.getFxImage());
                        BombermanGame.stillObject[j][i] = new Grass(j, i, Sprite.grass.getFxImage());
                        break;
                    case '1':
                        BombermanGame.enemies.add(new Balloon(j, i, Sprite.balloom_left1.getFxImage()));
                        BombermanGame.stillObject[j][i] = new Grass(j, i, Sprite.grass.getFxImage());
                        break;
                    case '2':
                        BombermanGame.enemies.add(new Oneal(j, i, Sprite.oneal_left1.getFxImage()));
                        BombermanGame.stillObject[j][i] = new Grass(j, i, Sprite.grass.getFxImage());
                        break;
                    case '3':
                        BombermanGame.enemies.add(new Minvo(j, i, Sprite.minvo_left1.getFxImage()));
                        BombermanGame.stillObject[j][i] = new Grass(j, i, Sprite.grass.getFxImage());
                        break;
                    case '4':
                        BombermanGame.enemies.add(new Kondoria(j, i, Sprite.kondoria_left1.getFxImage()));
                        BombermanGame.stillObject[j][i] = new Grass(j, i, Sprite.grass.getFxImage());
                        break;
                    // case '4': case '5':       // Kondoria,Doll,...
                    //     break;

                    // Still:
                    case '#':
                        BombermanGame.stillObject[j][i] = new Wall(j, i, Sprite.wall.getFxImage());
                        walls.add(new Wall(j, i, Sprite.wall.getFxImage()));
                        break;
                    case '*':
                        BombermanGame.stillObject[j][i] = new LayeredEntity(j, i,
                                new Grass(j, i, Sprite.grass.getFxImage()),
                                new Brick(j, i, Sprite.brick.getFxImage()));
                        bricks.add(new Brick(j, i, Sprite.brick.getFxImage()));
                        break;
                    case 'x':
                        BombermanGame.stillObject[j][i] = new LayeredEntity(j, i,
                                new Item(j, i, Sprite.portal.getFxImage(), Item.PortalItem),
                                new Brick(j, i, Sprite.brick.getFxImage()));
                        bricks.add(new Brick(j, i, Sprite.brick.getFxImage()));
                        break;
                    case 's':
                        BombermanGame.stillObject[j][i] = new LayeredEntity(j, i,
                                new Grass(j, i, Sprite.grass.getFxImage()),
                                new Item(j, i, Sprite.powerup_speed.getFxImage(), Item.SpeedItem),
                                new Brick(j, i, Sprite.brick.getFxImage()));
                        bricks.add(new Brick(j, i, Sprite.brick.getFxImage()));
                        break;
                    case 'b':
                        BombermanGame.stillObject[j][i] = new LayeredEntity(j, i,
                                new Grass(j, i, Sprite.grass.getFxImage()),
                                new Item(j, i, Sprite.powerup_bombs.getFxImage(), Item.BombItem),
                                new Brick(j, i, Sprite.brick.getFxImage()));
                        bricks.add(new Brick(j, i, Sprite.brick.getFxImage()));
                        break;
                    case 'f':
                        BombermanGame.stillObject[j][i] = new LayeredEntity(j, i,
                                new Grass(j, i, Sprite.grass.getFxImage()),
                                new Item(j, i, Sprite.powerup_flames.getFxImage(), Item.FlameItem),
                                new Brick(j, i, Sprite.brick.getFxImage()));
                        bricks.add(new Brick(j, i, Sprite.brick.getFxImage()));
                        break;
                    case 'w':
                        BombermanGame.stillObject[j][i] = new LayeredEntity(j, i,
                                new Grass(j, i, Sprite.grass.getFxImage()),
                                new Item(j, i, Sprite.powerup_wallpass.getFxImage(), Item.WallPassItem),
                                new Brick(j, i, Sprite.brick.getFxImage()));
                        bricks.add(new Brick(j, i, Sprite.brick.getFxImage()));
                        break;
                    case 'F':
                        BombermanGame.stillObject[j][i] = new LayeredEntity(j, i,
                                new Grass(j, i, Sprite.grass.getFxImage()),
                                new Item(j, i, Sprite.powerup_flamepass.getFxImage(), Item.FlamePassItem),
                                new Brick(j, i, Sprite.brick.getFxImage()));
                        bricks.add(new Brick(j, i, Sprite.brick.getFxImage()));
                        break;
                    default:
                        BombermanGame.stillObject[j][i] = new Grass(j, i, Sprite.grass.getFxImage());
                        break;
                }
            }
        }
    }

    public void clearMap() {
        BombermanGame.bombs.clear();
        BombermanGame.enemies.clear();
        BombermanGame.stillObject = null;
        // BombermanGame.player = null;
        walls.clear();
        bricks.clear();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
