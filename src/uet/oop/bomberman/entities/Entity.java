package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Gồm: + StillObject
 *      + MovableObject
 *      + Bomb, Flame (bomb)
 *      + LayeredEntity
 */
public abstract class Entity {
    protected static final boolean REMOVED = false;
    protected static final boolean EXIST = true;
    protected static final int MAX_ANIMATE = 10000; // 7500

    protected int x;            // Tọa độ X tính từ góc trái trên trong Canvas
    protected int y;            // Tọa độ Y tính từ góc trái trên trong Canvas
    protected Image img;
    protected boolean status;   // ktra entity còn trên map hay cần xóa bỏ
    protected int animate;      // Hiệu ứng cho MovableObject && Bomb


    // Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    // public Entity(int xUnit, int yUnit, Image img) {
    //     this.x = xUnit * Sprite.SCALED_SIZE;
    //     this.y = yUnit * Sprite.SCALED_SIZE;
    //     this.img = img;
    //     status = EXIST;
    //     animate = 0;
    // }

    public void render(GraphicsContext gc) {
        if (img == null) return;
        gc.drawImage(img, x, y);
    }

    public abstract void update();

    // ktra va chạm và có thể xử lý luôn trong hàm
    public abstract boolean checkCollision(Entity e);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public boolean isExist() {
        return status == EXIST;
    }

    public boolean isRemoved() {
        return status == REMOVED;
    }

    public void setExist() {
        this.status = EXIST;
    }

    public void setRemoved() {
        this.status = REMOVED;
    }
}
