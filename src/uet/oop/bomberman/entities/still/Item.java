package uet.oop.bomberman.entities.still;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.movable.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.AudioGame;

public class Item extends StillObject {
    public static final int BombItem = 1;
    public static final int FlameItem = 2;
    public static final int SpeedItem = 3;
    public static final int PortalItem = 4;
    public static final int WallPassItem = 5;
    public static final int FlamePassItem = 6;

    private int type;
    private boolean open;

    public Item(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Item(int xUnit, int yUnit, Image img, int type) {
        this(xUnit, yUnit, img);
        this.type = type;
        this.open = false;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext gc) {
        Grass background = new Grass(x / Sprite.SCALED_SIZE, y / Sprite.SCALED_SIZE, Sprite.grass.getFxImage());
        background.render(gc);
        gc.drawImage(img, x, y);
    }

    @Override
    public boolean checkCollision(Entity e) {       // true = có va chạm -> k đi qua được
        if (this.isRemoved()) return false;
        if (e instanceof Bomber) {
            switch (type) {
                case BombItem:
                    Bomber.bombLimit++;
                    break;
                case FlameItem:
                    Bomber.flameRadius++;
                    break;
                case SpeedItem:
                    Bomber.speed++;
                    break;
                case PortalItem:
                    if (BombermanGame.enemies.size() > 0) return false;
                    else {
                        BombermanGame.nextLevel();
                        return false;
                    }
                case WallPassItem:
                    Bomber.wallPass = true;
                    break;
                case FlamePassItem:
                    Bomber.flamePass = true;
                    break;
                default:
                    break;
            }
            this.setRemoved();

            // sound: type != portal
            AudioGame getItem = new AudioGame(AudioGame.GET_ITEM);
            getItem.playAudio();

            return false;
        }

        // if (e instanceof Minvo) return false;
        if (e instanceof Flame) return false;
        return true;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
