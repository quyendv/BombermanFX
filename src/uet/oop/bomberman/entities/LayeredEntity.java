package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.still.Brick;
import uet.oop.bomberman.entities.still.Grass;
import uet.oop.bomberman.entities.still.Item;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Stack;

/**
 * 1 đối tượng chứa nhiều đối tượng lồng nhau
 * VD: Brick->Item->Grass, Brick->Grass
 */
public class LayeredEntity extends Entity {
    private Stack<Entity> entityManage = new Stack<>();     // <StillObject>
    private Grass background = new Grass(0, 0, Sprite.grass.getFxImage());

    public LayeredEntity(int xUnit, int yUnit, Entity... e) {
        this.x = xUnit * Sprite.SCALED_SIZE;  // tọa độ trong mảng BombermanGame.StillObject
        this.y = yUnit * Sprite.SCALED_SIZE;  // tọa độ trong mảng BombermanGame.StillObject
        this.status = EXIST;
        for (int i = 0; i < e.length; i++) {
            if (e[i] != null) {
                entityManage.push(e[i]);
            }
        }
    }

    @Override
    public void update() {
        clearRemovedEntity();
        lastEntity().update();
    }

    @Override
    public boolean checkCollision(Entity e) {
        return lastEntity().checkCollision(e);
    }

    @Override
    public void render(GraphicsContext gc) {
        // Nếu là Item or Brick destroyed thì lót cỏ ở dưới
        if (lastEntity() instanceof Item || (lastEntity() instanceof Brick && ((Brick) lastEntity()).isDestroyed())){
            background.setX(lastEntity().getX());
            background.setY(lastEntity().getY());
            background.render(gc);
        }
        gc.drawImage(lastEntity().img, lastEntity().getX(), lastEntity().getY());
    }

    private void clearRemovedEntity() {
        if (lastEntity().isRemoved()) entityManage.pop();
    }

    public Entity lastEntity() {
        // entity luôn có grass ở đầu nên !empty()
        return entityManage.peek();
    }
}
