package com.example.CrazyRoadGame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView main_IMG_background;
    private ImageView[] main_IMG_allLife;
    private ImageView[][] main_IMG_rockMat;
    private ImageView[] main_IMG_playerPos;
    private ImageButton[] main_BTN_arrows;
    private ArrayList<String> mat_IMG_link;

    private Player thePlayer;
    private ArrayList<Rock> allRocks;
    private static final int ROCK_MAT_ROW = 8;
    private static final int ROCK_MAT_COL = 5;

    private static final long ROCK_DELAY = 1000;     //1000ms=1s
    private Handler rockHandler = new Handler();
    private Runnable rockCreateRunnable = new Runnable() {
        @Override
        public void run() {

            Rock rock = new Rock();
            allRocks.add(rock);
            for (int i = 0; i < allRocks.size(); i++) {
                if (allRocks.get(i) == null)
                    continue;
                else if (allRocks.get(i).getRow() <= ROCK_MAT_ROW) {
                    updateRockUI(allRocks.get(i).getRow(), allRocks.get(i).getCol());
                    if (allRocks.get(i).getRow() == ROCK_MAT_ROW) {
                        if (checkHit(allRocks.get(i)) == 1)
                            break;
                        allRocks.remove(i);
                        i--;
                    } else
                        allRocks.get(i).setRowNextLevel();
                }
            }
            if (thePlayer.getNumOfLife() == 0) {
                rockHandler.removeCallbacks(rockCreateRunnable);
                setDefaultInvisible();
                onStart();
            } else
                rockHandler.postDelayed(this, ROCK_DELAY);       //r run again after delay(1s)
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create_mat_option();
        findViews();
        initViews();
    }


    @Override
    protected void onStart() {
        super.onStart();

        setDefaultInvisible();
        thePlayer = new Player();
        allRocks = new ArrayList<Rock>();
        toast("start game");
        startRockFlow();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void create_mat_option() {
        //0-invisible   1-poop  2-coin
        mat_IMG_link = new ArrayList<>();
        mat_IMG_link.add("");
        mat_IMG_link.add("https://img.icons8.com/emoji/48/000000/pile-of-poo.png");
        mat_IMG_link.add("https://img.icons8.com/emoji/48/000000/coin-emoji.png");

    }

    private void findViews() {

        main_IMG_background = findViewById(R.id.main_IMG_background);

        main_IMG_allLife = new ImageView[]{
                findViewById(R.id.main_BTN_life1),
                findViewById(R.id.main_BTN_life2),
                findViewById(R.id.main_BTN_life3)
        };

        main_IMG_rockMat = new ImageView[][]{
                {findViewById(R.id.main_IMG_rock00), findViewById(R.id.main_IMG_rock01), findViewById(R.id.main_IMG_rock02), findViewById(R.id.main_IMG_rock03), findViewById(R.id.main_IMG_rock04)},
                {findViewById(R.id.main_IMG_rock10), findViewById(R.id.main_IMG_rock11), findViewById(R.id.main_IMG_rock12), findViewById(R.id.main_IMG_rock13), findViewById(R.id.main_IMG_rock14)},
                {findViewById(R.id.main_IMG_rock20), findViewById(R.id.main_IMG_rock21), findViewById(R.id.main_IMG_rock22), findViewById(R.id.main_IMG_rock23), findViewById(R.id.main_IMG_rock24)},
                {findViewById(R.id.main_IMG_rock30), findViewById(R.id.main_IMG_rock31), findViewById(R.id.main_IMG_rock32), findViewById(R.id.main_IMG_rock33), findViewById(R.id.main_IMG_rock34)},
                {findViewById(R.id.main_IMG_rock40), findViewById(R.id.main_IMG_rock41), findViewById(R.id.main_IMG_rock42), findViewById(R.id.main_IMG_rock43), findViewById(R.id.main_IMG_rock44)},
                {findViewById(R.id.main_IMG_rock50), findViewById(R.id.main_IMG_rock51), findViewById(R.id.main_IMG_rock52), findViewById(R.id.main_IMG_rock53), findViewById(R.id.main_IMG_rock54)},
                {findViewById(R.id.main_IMG_rock60), findViewById(R.id.main_IMG_rock61), findViewById(R.id.main_IMG_rock62), findViewById(R.id.main_IMG_rock63), findViewById(R.id.main_IMG_rock64)},
                {findViewById(R.id.main_IMG_rock70), findViewById(R.id.main_IMG_rock71), findViewById(R.id.main_IMG_rock72), findViewById(R.id.main_IMG_rock73), findViewById(R.id.main_IMG_rock74)}
        };

        main_IMG_playerPos = new ImageView[]{
                findViewById(R.id.main_IMG_playerPos0),
                findViewById(R.id.main_IMG_playerPos1),
                findViewById(R.id.main_IMG_playerPos2),
                findViewById(R.id.main_IMG_playerPos3),
                findViewById(R.id.main_IMG_playerPos4)
        };

        main_BTN_arrows = new ImageButton[]{
                findViewById(R.id.main_BTN_left_arrow),
                findViewById(R.id.main_BTN_right_arrow)

        };
    }

    private void initViews() {

        //set background image
        Glide
                .with(this)
                .load(R.drawable.background1)
                .centerCrop()
                .into(main_IMG_background);


        //set life IMG
        for (int i = 0; i < main_IMG_allLife.length; i++) {
            Glide
                    .with(this)
                    .load(R.drawable.heart_full)
                    .into(main_IMG_allLife[i]);
        }

        //set rock IMG
        for (int i = 0; i < ROCK_MAT_ROW; i++) {
            for (int j = 0; j < ROCK_MAT_COL; j++) {
                main_IMG_rockMat[i][j].setImageResource(R.drawable.poop);
            }
        }


        //set player IMG
        for (int i = 0; i < main_IMG_playerPos.length; i++) {
            Glide
                    .with(this)
                    .load(R.drawable.car1)
                    .centerInside()
                    .into(main_IMG_playerPos[i]);
        }


        //set left arrows IMG
        Glide
                .with(this)
                .load(R.drawable.left_arrow)
                .into(main_BTN_arrows[0]);

        //set right arrows IMG
        Glide
                .with(this)
                .load(R.drawable.right_arrow)
                .into(main_BTN_arrows[1]);


        //setOnClickListener - arrows
        for (int i = 0; i < main_BTN_arrows.length; i++) {
            int finalI = i;
            main_BTN_arrows[i].setOnClickListener(v -> {
                arrowClicked(finalI);
            });
        }
    }

    private void arrowClicked(int clickedPos) {
        int nextPos;
        int currentPos = thePlayer.getPos();

        if (clickedPos == 0) {
            if (currentPos == 0)
                nextPos = currentPos;
            else
                nextPos = currentPos - 1;
        }
        else{
            if (currentPos == ROCK_MAT_COL - 1)
                nextPos = currentPos;
            else
                nextPos = currentPos + 1;
        }

        updatePlayerUI(currentPos, nextPos);
        thePlayer.setPos(nextPos);

    }

    private void updatePlayerUI(int currentPos, int nextPos) {
        main_IMG_playerPos[currentPos].setVisibility(View.INVISIBLE);
        main_IMG_playerPos[nextPos].setVisibility(View.VISIBLE);
    }

    private void setDefaultInvisible() {

        //set all rocks INVISIBLE
        for (int i = 0; i < ROCK_MAT_ROW; i++) {
            for (int j = 0; j < ROCK_MAT_COL; j++) {
                main_IMG_rockMat[i][j].setVisibility(View.INVISIBLE);
            }
        }

        //set only the center player visible
        main_IMG_playerPos[0].setVisibility(View.INVISIBLE);
        main_IMG_playerPos[1].setVisibility(View.INVISIBLE);
        main_IMG_playerPos[2].setVisibility(View.VISIBLE);
        main_IMG_playerPos[3].setVisibility(View.INVISIBLE);
        main_IMG_playerPos[4].setVisibility(View.INVISIBLE);

        for (int i = 0; i < main_IMG_allLife.length; i++) {
            main_IMG_allLife[i].setVisibility(View.VISIBLE);
        }
    }

    private void updateRockUI(int rockRow, int rockCol) {
        if (rockRow != 0)
            main_IMG_rockMat[rockRow - 1][rockCol].setVisibility(View.INVISIBLE);
        if (rockRow < ROCK_MAT_ROW) {
            main_IMG_rockMat[rockRow][rockCol].setVisibility(View.VISIBLE);
        }
    }

    private void startRockFlow() {
        rockHandler.postDelayed(rockCreateRunnable, ROCK_DELAY);
    }

    private int checkHit(Rock currentRock) {
        int isGameOver = 0;
        if (currentRock.getCol() == thePlayer.getPos()) {
            thePlayer.setLifeAfterHit();
            updateLifeUI();
            if (thePlayer.getNumOfLife() == 0) {
                isGameOver = 1;
            }
        }
        return isGameOver;
    }

    private void updateLifeUI() {
        main_IMG_allLife[thePlayer.getNumOfLife()].setVisibility(View.INVISIBLE);
        vibrate();
        toast("hoe shit!");
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}