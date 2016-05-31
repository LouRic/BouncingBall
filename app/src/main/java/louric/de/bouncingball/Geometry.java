package louric.de.bouncingball;

public class Geometry {

    public static class Vector {
        public float x = 0;
        public float y = 0;

        public Vector(float x, float y){
            this.x = x;
            this.y = y;
        }

        public void multiplyBy(float f){
            this.x *= f;
            this.y *= f;
        }

        public float length(){  return (float) Math.sqrt(x*x + y*y);  }

        public float scalar(Vector vec){
            return (this.x * vec.x) + (this.y * vec.y);
        }

        public void rotateBy(float alpha){
            float cos = (float) Math.cos(alpha);
            float sin = (float) Math.sin(alpha);
            float x = this.x * cos - this.y * sin;
            float y = this.x * sin + this.y * cos;
            this.x = x;
            this.y = y;
        }

        public void toUnitvec(){
            float l = this.length();
            this.x = this.x / l;
            this.y = this.y / l;
        }
    }

    public static Vector randomVector() {
        float x = (float) (1 - (Math.random() * 2));
        float y = (float) (1 - (Math.random() * 2));
        Vector v = new Vector(x, y);
        v.toUnitvec();
        return v;
    }

    public static Vector vectorFromTo(float x1, float y1, float x2, float y2) {
        return new Vector(x2-x1, y2-y1);
    }

    public static float distanceFromTo(float x1, float y1, float x2, float y2) {
        float x = x2 - x1;
        float y = y2 - y1;
        return (float) Math.sqrt(x*x + y*y);
    }

    public float angleFromTo(Vector a, Vector b){
        float zaehler = a.scalar(b);
        float nenner = a.length() * b.length();
        return (float) Math.acos(zaehler / nenner);
    }
}
