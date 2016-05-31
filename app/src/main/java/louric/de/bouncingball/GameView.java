package louric.de.bouncingball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Toast;

public class GameView extends View implements View.OnClickListener {
    public MainActivity activity = null;

    public static int WIDTH;
    public static int HEIGHT;

    public Ball[] balls = null;
    public BonusBalls bonusBalls = new BonusBalls();
    public PlayerBall player = null;
    public Paint paint = new Paint();

    public int background_color = Color.parseColor("#000000");
    public int text_color = Color.parseColor("#ffffff");

    public GameView(MainActivity activity) {
        super(activity.getApplicationContext());

        player = new PlayerBall(0, 0, (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE));

        this.activity = activity;

        this.setOnClickListener(this);
    }

    public void incSpeed(){
        Ball.speed = Ball.speed * 1.01f;
        Ball.speed = Ball.speed + 0.1f;
        for (Ball ball:balls) {
            ball.vec.toUnitvec();
            ball.vec.multiplyBy(Ball.speed);
        }
    }

    private void firstDrawCall(){
        balls = new Ball[4];

        WIDTH = getWidth();
        HEIGHT = getHeight();

        int w = WIDTH;
        int h = HEIGHT;
        int dw = w / 4;
        int dh = h / 4;

        player.x = w / 2;
        player.y = h / 2;

        balls[0] = new Ball(1, 40, dw, dh);
        balls[1] = new Ball(2, 40, w - dw, dh);
        balls[2] = new Ball(3, 40, dw, h - dh);
        balls[3] = new Ball(4, 40, w - dw, h - dh);

        for (Ball ball : balls) {
            ball.randomVec();
        }

        paint.setTextSize(50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (balls == null) {
            firstDrawCall();
        }

        if (Math.random() < bonusBalls.getSpawnPropability()) {
            bonusBalls.newBall();
        }

        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(background_color);
        canvas.drawPaint(paint);

        // Spielerleben anzeigen
        paint.setColor(text_color);
        canvas.drawText("Leben: "+player.health, 100, 100, paint);
        canvas.drawText("Score: "+player.score, WIDTH - 300, 100, paint);

        // Bonusbälle zeichnen
        for (int i = 0; i<bonusBalls.counter; i++) {
            BonusBalls.BonusBall b = bonusBalls.balls[i];
            b.update(0, 0, WIDTH, HEIGHT);
            paint.setColor(b.color);
            canvas.drawCircle(b.x, b.y, b.radius, paint);
        }

        // Spielerball zeichnen
        paint.setColor(player.color);
        canvas.drawCircle(player.x, player.y, player.radius, paint);

        for (Ball A : balls) {

            // Ball Zeichnen
            paint.setColor(A.color);
            canvas.drawCircle(A.x, A.y, A.radius, paint);

            for (Ball B : balls) {
                if (A != B) {
                    if (Geometry.distanceFromTo(A.x, A.y, B.x, B.y) <= A.radius + B.radius) {
                        Geometry.Vector temp = A.vec;
                        A.vec = B.vec;
                        B.vec = temp;

                        A.updatePos();
                        B.updatePos();

                        incSpeed();
                    }
                }
            }

            A.update(0, 0, WIDTH, HEIGHT);
        }

        for (Ball ball : balls) {
            if (Geometry.distanceFromTo(ball.x, ball.y, player.x, player.y) <= player.radius + ball.radius) {
                player.health--;
                if (player.health == 0) {
                    activity.gameOver(player);
                    return;
                }

                if (player.vec.length() >= Ball.speed) {
                    Geometry.Vector oldBallVec = new Geometry.Vector(ball.vec.x, ball.vec.y);

                    ball.vec.x = player.vec.x;
                    ball.vec.y = player.vec.y;
                    ball.vec.toUnitvec();
                    ball.vec.multiplyBy(Ball.speed);
                    ball.updatePos();

                    player.collison(oldBallVec);
                } else {
                    player.collison(new Geometry.Vector(ball.vec.x, ball.vec.y));
                    ball.vec.multiplyBy(-1);
                }
            }
        }

        bonusBalls.checkHit(player);

        player.update(0, 0, getWidth(), getHeight());

        /* try                             { Thread.sleep(1000 / 120);            }
           catch (InterruptedException ex) { System.err.println(ex.getMessage()); } */

        invalidate();
    }

    @Override
    public void onClick(View v) {
        player.reset(getWidth() / 2, getHeight() / 2);
        Toast t = Toast.makeText(activity.getApplicationContext(), "Reset!", Toast.LENGTH_SHORT);
        t.show();
    }
}
