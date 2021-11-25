package com.example.CrazyRoadGame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioRecord;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView main_IMG_background;
    private ImageView[] main_IMG_allLife;
    private ImageView[][] main_IMG_rockMat;
    private ImageView[] main_IMG_playerPos;
    private ImageButton[] main_BTN_arrows;
    private ArrayList<Integer> mat_IMG_id;
    private TextView main_TXT_score;

    private Player thePlayer;
    private ArrayList<FlowingItem> allItems;
    private static final int ROCK_MAT_ROW = 8;
    private static final int ROCK_MAT_COL = 5;

    private static final long ROCK_DELAY = 1000;     //1000ms=1s
    private Handler rockHandler = new Handler();
    private Runnable rockCreateRunnable = new Runnable() {
        @Override
        public void run() {

            updateUiScore();
            createCoin();
            createRock();
            dropItem();

            if (thePlayer.getNumOfLife() == 0) {                     //game over
                rockHandler.removeCallbacks(rockCreateRunnable);
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
        allItems = new ArrayList<FlowingItem>();
        updateUiScore();
        toast("Start game");
        rockHandler.postDelayed(rockCreateRunnable, ROCK_DELAY);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void create_mat_option() {
        //   0-poop  1-coin
        mat_IMG_id = new ArrayList<>();
        mat_IMG_id.add(R.drawable.poop);
        mat_IMG_id.add(R.drawable.coin1);

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

        main_TXT_score = findViewById(R.id.main_TXT_score);

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

     /*
        //set rock IMG
        for (int i = 0; i < ROCK_MAT_ROW; i++) {
            for (int j = 0; j < ROCK_MAT_COL; j++) {
                main_IMG_rockMat[i][j].setImageResource(R.drawable.poop);
            }
        }
*/

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

    private void updateUiScore() {
        main_TXT_score.setText("score:"+thePlayer.getScore());
    }

    private void arrowClicked(int clickedPos) {
        int nextPos;
        int currentPos = thePlayer.getPos();

        if (clickedPos == 0) {
            if (currentPos == 0)
                nextPos = currentPos;
            else
                nextPos = currentPos - 1;
        } else {
            if (currentPos == ROCK_MAT_COL - 1)
                nextPos = currentPos;
            else
                nextPos = currentPos + 1;
        }

        updatePlayerUI(currentPos, nextPos);
        thePlayer.setPos(nextPos);

    }

    private void createRock() {
        FlowingItem rock = new FlowingItem(FlowingItem.ITEM_TYPE.ROCK);
        allItems.add(rock);
    }

    private void createCoin() {
        Random r = new Random();
        if (r.nextInt(13) < 4) {
            FlowingItem coin = new FlowingItem(FlowingItem.ITEM_TYPE.COIN);
            allItems.add(coin);
        }
    }

    private void dropItem() {

        for (int i = 0; i < allItems.size(); i++) {
            allItems.get(i).setRowNextLevel();
            if (allItems.get(i).getRow() <= ROCK_MAT_ROW) {
                dropItemOnUI(allItems.get(i));
                if (allItems.get(i).getRow() == ROCK_MAT_ROW) {
                    checkHit(allItems.get(i));
                    if (thePlayer.getNumOfLife() == 0)
                        break;
                    allItems.remove(i);
                    i--;
                }
            }
        }
    }

    private void updatePlayerUI(int currentPos, int nextPos) {
        main_IMG_playerPos[currentPos].setVisibility(View.INVISIBLE);
        main_IMG_playerPos[nextPos].setVisibility(View.VISIBLE);
    }

    private void dropItemOnUI(FlowingItem currentItem) {
        int ItemRow = currentItem.getRow();
        int ItemCol = currentItem.getCOL();

        if (ItemRow != 0)
            main_IMG_rockMat[ItemRow - 1][ItemCol].setVisibility(View.INVISIBLE);
        if (ItemRow < ROCK_MAT_ROW) {
            main_IMG_rockMat[ItemRow][ItemCol].setVisibility(View.VISIBLE);
            Glide
                    .with(this)
                    .load(mat_IMG_id.get(currentItem.getITEM_TYPE().ordinal()))
                    .into(main_IMG_rockMat[ItemRow][ItemCol]);
        }
    }

    private void updateLifeUI() {
        main_IMG_allLife[thePlayer.getNumOfLife()].setVisibility(View.INVISIBLE);
        vibrate();
        toast("hoe shit!");
    }

    private void checkHit(FlowingItem currentItem) {

        if (currentItem.getCOL() == thePlayer.getPos()) {       //hit
            switch (currentItem.getITEM_TYPE()) {
                case ROCK:
                    thePlayer.hitRock();
                    updateLifeUI();
                    break;
                case COIN:
                    thePlayer.hitCoin(1);
                    break;
            }
        }
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