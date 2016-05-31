package louric.de.bouncingball;


import android.graphics.Color;

import static louric.de.bouncingball.Geometry.randomVector;

public class Ball {
    public static float START_SPEED = 5f;
    public static float speed = START_SPEED;
    public static String COLOR = "#ff9900";

    public float x = 0;
    public float y = 0;

    public Geometry.Vector vec;

    public int radius = 0;
    public int color = 0;

    public int ID;

    public Ball(int id, int radius, float x, float y){
        this.radius = radius;
        this.setColor(COLOR);
        this.setPosition(x, y);
        this.vec = new Geometry.Vector(0, 0);
        this.ID = id;
    }

    public void setColor(String c){
        this.color = Color.parseColor(c);
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void update(int minX, int minY, int maxX, int maxY){
        int hr = (radius / 2) + 25;
        if ((x <= minX+hr && vec.x < 0) || (x >= maxX-hr && vec.x > 0)) {
            vec.x *= -1;
            x += vec.x;
        }
        if ((y <= minY+hr && vec.y < 0) || (y >= maxY-hr && vec.y > 0)) {
            vec.y *= -1;
            y += vec.y;
        }

        this.updatePos();
    }

    public void updatePos(){
        x += this.vec.x;
        y += this.vec.y;
    }

    public void randomVec(){
        this.vec = randomVector();
        this.vec.multiplyBy(Ball.speed);
    }

    public static void reset(){
        speed = START_SPEED;
    }
}
