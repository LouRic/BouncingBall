package louric.de.bouncingball;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/*
 * TODO:
 * - Drift beim aufeinanderprallen
 * - End/Start-Screen verbessern
 * - Abprallen von 2 orangenen Bällen physikalisch korrekt.
 *
 * BUGFIX:
 * - Wenn man den Rand berührt und der Spieler das Handy "zum Rand" drückt wird das in sumx bzw. sumy
 *   reingezähöt ohne das sich der Ball bewegt => Komisch sobald man wieder vom Rand weg schwenkt.
 */

public class MainActivity extends Activity implements View.OnClickListener {
    /** Called when the activity is first created. */

    private Button startButton = null;
    private TextView textView = null;
    private TextView scoreView = null;
    private TextView highscoreView = null;

    public int highscore = 0;

    private static final String PREFS_NAME = "my-prefs-file";
    private SharedPreferences prefs = null;

    public void findViews(){
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.textView);

        scoreView = (TextView) findViewById(R.id.player_score);

        highscoreView = (TextView) findViewById(R.id.textView_highscore);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("MainActivity.onCreate!");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        prefs = getSharedPreferences(PREFS_NAME, 0);
        highscore = prefs.getInt("highscore", -1);
        if (highscore >= 0) {
            highscoreView.setText("Highscore: "+highscore);
        }
    }

    public void startGame(){
        System.out.println("START GAME!");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(new GameView(this));
        Ball.reset();
    }

    public void gameOver(PlayerBall player){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        findViews();

        System.out.println("GAME OVER!");

        textView.setText("Game Over!");
        textView.setTextColor(Color.parseColor("#ff0000"));
        startButton.setText("Restart Game!");

        scoreView.setText("Dein Score: "+player.score);

        if (player.score > highscore) {
            highscore = player.score;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", highscore);
            editor.commit();

            highscoreView.setText("Du hast den Highscore geknackt!");
        } else {
            highscore = prefs.getInt("highscore", -1);
            if (highscore >= 0) {
                highscoreView.setText("Highscore: "+highscore);
            } else {
                highscoreView.setText("");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == startButton.getId()) {
            startGame();
        }
    }
}
