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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView[] main_IMG_allLife;
    private Player thePlayer;
    private ImageView[][] main_IMG_rockMat;
    private ImageView[] main_IMG_playerPos;
    private ImageButton[] main_BTN_arrows;
    private static final int ROCK_MAT_ROW = 5;
    private static final int ROCK_MAT_COL = 3;
    private static final long ROCK_DELAY = 1200;     //1000ms=1s
    private ArrayList<Rock> allRocks;
    private Handler rockHandler = new Handler();
    private Runnable rockCreateRunnable = new Runnable() {
        @Override
        public void run() {

            Rock rock = new Rock();
            allRocks.add(rock);
            for (int i = 0; i < allRocks.size(); i++) {
                if (allRocks.get(i) == null)
                    continue;
                else if (allRocks.get(i).getRow() <= 5) {
                    updateRockUI(allRocks.get(i).getRow(), allRocks.get(i).getCol());
                    if (allRocks.get(i).getRow() == 5) {
                        if(checkHit(allRocks.get(i))==1)
                            break;
                        allRocks.remove(i);
                        i--;
                    }
                    else
                        allRocks.get(i).setRowNextLevel();
                }
            }
            if(thePlayer.getNumOfLife()==0) {
                rockHandler.removeCallbacks(rockCreateRunnable);
                setDefaultInvisible();
                onStart();
            }
            else
                rockHandler.postDelayed(this, ROCK_DELAY);       //r run again after delay(1s)


        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


    private void findViews() {

        main_IMG_allLife = new ImageView[]{
                findViewById(R.id.main_BTN_life1),
                findViewById(R.id.main_BTN_life2),
                findViewById(R.id.main_BTN_life3)
        };

        main_IMG_rockMat = new ImageView[][]{
                {findViewById(R.id.main_IMG_rock00), findViewById(R.id.main_IMG_rock01), findViewById(R.id.main_IMG_rock02)},
                {findViewById(R.id.main_IMG_rock10), findViewById(R.id.main_IMG_rock11), findViewById(R.id.main_IMG_rock12)},
                {findViewById(R.id.main_IMG_rock20), findViewById(R.id.main_IMG_rock21), findViewById(R.id.main_IMG_rock22)},
                {findViewById(R.id.main_IMG_rock30), findViewById(R.id.main_IMG_rock31), findViewById(R.id.main_IMG_rock32)},
                {findViewById(R.id.main_IMG_rock40), findViewById(R.id.main_IMG_rock41), findViewById(R.id.main_IMG_rock42)}
        };

        main_IMG_playerPos = new ImageView[]{
                findViewById(R.id.main_IMG_playerLeft),
                findViewById(R.id.main_IMG_playerCenter),
                findViewById(R.id.main_IMG_playerRight)
        };

        main_BTN_arrows = new ImageButton[]{
                findViewById(R.id.main_BTN_left_arrow),
                findViewById(R.id.main_BTN_right_arrow)

        };
    }

    private void initViews() {

        //set life IMG
        for (ImageView temp : main_IMG_allLife) {
            temp.setImageResource(R.drawable.heart_full);
        }

        //set rock IMG
        for (int i = 0; i < ROCK_MAT_ROW; i++) {
            for (int j = 0; j < ROCK_MAT_COL; j++) {
                main_IMG_rockMat[i][j].setImageResource(R.drawable.poop);
            }
        }


        //set player IMG
        for (ImageView temp : main_IMG_playerPos) {
            temp.setImageResource(R.drawable.car1);
        }


        //set arrows IMG
        main_BTN_arrows[0].setImageResource(R.drawable.left_arrow);
        main_BTN_arrows[1].setImageResource(R.drawable.right_arrow);


        for (int i = 0; i < main_BTN_arrows.length; i++) {
            int finalI = i;
            main_BTN_arrows[i].setOnClickListener(v -> {
                arrowClicked(finalI);
            });
        }
    }


    private void arrowClicked(int finalI) {
        Player.POS nextPos;
        Player.POS currentPos = thePlayer.getPos();

        if (finalI == 0)
            nextPos = Player.POS.LEFT;
        else
            nextPos = Player.POS.RIGHT;


        if (Math.abs(currentPos.ordinal() - nextPos.ordinal()) > 1) {
            nextPos = Player.POS.CENTER;
            updatePlayerUI(currentPos, nextPos);
            thePlayer.setPos(nextPos);
        } else if (nextPos != currentPos) {
            updatePlayerUI(currentPos, nextPos);
            thePlayer.setPos(nextPos);
        }
    }

    private void updatePlayerUI(Player.POS currentPos, Player.POS nextPos) {
        main_IMG_playerPos[currentPos.ordinal()].setVisibility(View.INVISIBLE);
        main_IMG_playerPos[nextPos.ordinal()].setVisibility(View.VISIBLE);
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
        main_IMG_playerPos[1].setVisibility(View.VISIBLE);
        main_IMG_playerPos[2].setVisibility(View.INVISIBLE);

        for(int i=0;i<main_IMG_allLife.length;i++){
            main_IMG_allLife[i].setVisibility(View.VISIBLE);
        }
    }


    private void updateRockUI(int rockRow, int rockCol) {
        if (rockRow != 0)
            main_IMG_rockMat[rockRow - 1][rockCol].setVisibility(View.INVISIBLE);
        if (rockRow < 5) {
            main_IMG_rockMat[rockRow][rockCol].setVisibility(View.VISIBLE);
        }
    }

    private void startRockFlow() {
        rockHandler.postDelayed(rockCreateRunnable, ROCK_DELAY);
    }

    private int checkHit(Rock currentRock) {
        int isGameOver=0;
        if (currentRock.getCol() == thePlayer.getPos().ordinal()) {
            thePlayer.setLifeAfterHit();
            updateLifeUI();
            if (thePlayer.getNumOfLife() == 0) {
                isGameOver=1;
            }

        }
        return isGameOver;
    }

    private void updateLifeUI() {
        main_IMG_allLife[thePlayer.getNumOfLife()].setVisibility(View.GONE);
        vibrate();
        toast("you have been hit!");
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