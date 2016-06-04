package louric.de.bouncingball;

import static louric.de.bouncingball.Geometry.*;

public class BonusBalls {
    public static float PROBABILITY = 0.0025f;
    public static float SPEED = Ball.START_SPEED * 1.5f;

    public static class BonusBall extends Ball {

        public static String COLOR = "#52ff00";

        public BonusBall(int id, float x, float y){
            super(id, 15, x, y);
            setColor(COLOR);
        }
    }


    public float getSpawnPropability(){
        if (counter == 0) {
            return 1;
        } else {
            return PROBABILITY;
        }
    }


    public int counter = 0;
    public int max = 10;
    public BonusBall[] balls = new BonusBall[max];

    public BonusBalls(){}

    public BonusBall fromTopLeft(){
        BonusBall b = new BonusBall(10+counter, 0, 0);
        b.vec = new Vector((float) (0.5 + Math.random()), (float) (0.5 + Math.random()));
        b.vec.toUnitvec();
        b.vec.multiplyBy(SPEED);
        return b;
    }

    public BonusBall fromTopRight(){
        BonusBall b = new BonusBall(10+counter, GameView.WIDTH, 0);
        b.vec = new Vector(-(float) (0.5 + Math.random()), (float) (0.5 + Math.random()));
        b.vec.toUnitvec();
        b.vec.multiplyBy(SPEED);
        return b;
    }

    public BonusBall fromBottomLeft(){
        BonusBall b = new BonusBall(10+counter, 0, GameView.HEIGHT);
        b.vec = new Vector(-(float) (0.5 + Math.random()), (float) (0.5 + Math.random()));
        b.vec.toUnitvec();
        b.vec.multiplyBy(SPEED);
        return b;
    }

    public BonusBall fromBottomRight(){
        BonusBall b = new BonusBall(10+counter, GameView.WIDTH, GameView.HEIGHT);
        b.vec = new Vector(-(float) (0.5 + Math.random()), -(float) (0.5 + Math.random()));
        b.vec.toUnitvec();
        b.vec.multiplyBy(SPEED);
        return b;
    }

    public void newBall(){
        if (counter < max) {
            double rnd = Math.random();
            if (rnd < 0.25) {
                balls[counter] = fromTopLeft();
            } else if (rnd < 0.5) {
                balls[counter] = fromTopRight();
            } else if (rnd < 0.75) {
                balls[counter] = fromBottomLeft();
            } else {
                balls[counter] = fromBottomRight();
            }

            counter++;
        }
    }

    public void removeBonusBall(BonusBall b, int index) {
        balls[index] = balls[counter-1];
        balls[counter-1] = null;
        counter--;
    }

    public void checkHit(PlayerBall player){
        for (int i = 0; i<counter; i++) {
            BonusBall b = balls[i];
            if (distanceFromTo(b.x, b.y, player.x, player.y) <= player.radius - b.radius) {
                player.score++;
                player.radius += 1;
                removeBonusBall(b, i);
                break;
            }
        }
    }
}
