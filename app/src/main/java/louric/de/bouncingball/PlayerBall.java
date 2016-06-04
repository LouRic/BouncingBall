package louric.de.bouncingball;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class PlayerBall extends Ball implements SensorEventListener {
    static final int DEFAULT_PENALTY = 40;
    static final float MAX_SPEED = Ball.START_SPEED * 1.75f;
    static String Color = "#33ccff";

    public int health = 5;
    public int score = 0;

    private Sensor mSensor;
    private int penalty = 0;

    float ax = 0;
    float ay = 0;

    private float sumX = 0;
    private float sumY = 0;

    public PlayerBall(float x, float y, SensorManager mSensorManager){
        super(0, 50, x, y);
        setColor(COLOR);

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void update(int minX, int minY, int maxX, int maxY){

        if (penalty == 0) {
            this.vec.x = sumX;
            this.vec.y = sumY * -1;
            setColor("#0000ff");
        } else {
            penalty--;
        }

        if (this.vec.length() > MAX_SPEED) {
            this.vec.toUnitvec();
            this.vec.multiplyBy(MAX_SPEED);
        }

        int hr = (radius / 2) + 50;
        if (x <= minX+hr && vec.x < 0) {
            vec.x = 0;
            // sumX += ax;
        } else if (x >= maxX-hr && vec.x > 0) {
            vec.x = 0;
            // sumX += ax;
        }
        if (y <= minY+hr && vec.y < 0) {
            vec.y = 0;
            // sumY += ay;
        } else if (y >= maxY-hr && vec.y > 0) {
            vec.y = 0;
            // sumY += ay;
        }

        this.updatePos();
    }


    public void collison(Geometry.Vector newVec){
        penalty = DEFAULT_PENALTY;
        this.vec = newVec;
        setColor("#ff0000");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            ax = event.values[0];// * 0.75f;
            ay = event.values[1];// * 0.75f;

            if (penalty == 0) {
                sumX += ax;
                sumY += ay;
            }
        }
    }

    public void reset(float newX, float newY){
        sumX = 0;
        sumY = 0;
        this.x = newX;
        this.y = newY;
        this.vec.multiplyBy(0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("Accuracy Changed!");
    }
}
