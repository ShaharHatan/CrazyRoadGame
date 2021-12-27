package com.example.CrazyRoadGame.model.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
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
import com.example.CrazyRoadGame.R;
import com.example.CrazyRoadGame.model.splash.SplashActivity;

import java.util.ArrayList;
import java.util.Random;


public class GameActivity extends AppCompatActivity {

    private ImageView main_IMG_background;
    private ImageView[] main_IMG_allLife, main_IMG_playerPos;
    private ImageView[][] main_IMG_rockMat;
    private ImageButton[] main_BTN_arrows;
    private ArrayList<Integer> mat_IMG_id;
    private TextView main_TXT_score;

    private final int MOVE_LEFT = 0, MOVE_RIGHT = 1;
    private final double SENSOR_LIMIT_LEFT = 2, SENSOR_LIMIT_RIGHT = -2 , LIMIT_DELTA = 0.8;
    private SensorManager sensorManager;
    private Sensor sensor;
    private float currX, firstX,lastX;
    private boolean isFirstX;
    private SensorEventListener SEL = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (isFirstX) {
                firstX = event.values[0];
                isFirstX = false;
                currX=firstX;
            }
            // x = [0]      y = [1]     z = [2]
            lastX=currX;
            currX = event.values[0];
            float deltaByFirst = currX - firstX;
            float deltaByLast = currX-lastX;
            if(Math.abs(deltaByLast) > LIMIT_DELTA) {
                if (deltaByFirst >= SENSOR_LIMIT_LEFT) {
                    //then move car left
                    moveCar(MOVE_LEFT);
                } else if (deltaByFirst <= SENSOR_LIMIT_RIGHT) {
                    //then move car right
                    moveCar(MOVE_RIGHT);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private boolean isSensorMood;

    public static final int ROCK_DELAY_EASY = 1000;     //1000ms=1s
    public static final int ROCK_DELAY_HARD = 600;     //700ms=0.6s
    private final int ROCK_MAT_ROW = 8, ROCK_MAT_COL = 5;
    private Player thePlayer;
    private MediaPlayer sound;
    private ArrayList<FlowingItem> allItems;
    private Bundle bundle;
    private int speed;
    private Handler rockHandler = new Handler();
    private Runnable rockCreateRunnable = new Runnable() {
        @Override
        public void run() {
            createCoin();
            createRock();
            dropItem();

            if (thePlayer.getNumOfLife() == 0) {                     //game over
                rockHandler.removeCallbacks(rockCreateRunnable);
                gameDone();
            } else
                rockHandler.postDelayed(this, speed);       //r run again after delay(speed sec)
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create_mat_option();
        findViews();
        initViews();
        //To identify the sensors that are on a device you first need to
        // get a reference to the sensor service. To do this, you create
        // an instance of the SensorManager class by calling the
        // getSystemService() method and passing in the SENSOR_SERVICE argument.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bundle = getIntent().getBundleExtra("location");
        setDefaultVisibility();
        thePlayer = new Player();
        allItems = new ArrayList<FlowingItem>();
        isFirstX = true;
        updateUiScore();
        isSensorMood = bundle.getBoolean("isAccSensor", false);
        if (isSensorMood && isSensorExists(Sensor.TYPE_ACCELEROMETER)) {
            initSensor();
            toast("Start game on sensor mode");
        } else {
            initArrowButtons();
            toast("Start game on arrows mode");
        }
        speed = bundle.getInt("difficulty", ROCK_DELAY_EASY);
        rockHandler.postDelayed(rockCreateRunnable, speed);
    }


    private void initArrowButtons() {
        main_BTN_arrows = new ImageButton[]{
                findViewById(R.id.main_BTN_left_arrow),
                findViewById(R.id.main_BTN_right_arrow)
        };

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
        // 0 - left      1 - right
        for (int i = 0; i < main_BTN_arrows.length; i++) {
            int finalI = i;
            main_BTN_arrows[i].setOnClickListener(v -> {
                moveCar(finalI);
            });
        }
    }

    private void initSensor() {

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(SEL, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //check if the sensorType is exist on the device
    public boolean isSensorExists(int sensorType) {
        return (sensorManager.getDefaultSensor(sensorType) != null);
    }

    private void gameDone() {
        sound.stop();
       unregisterSensor();
        bundle.putInt("score", thePlayer.getScore());
        Intent intentGame = new Intent(this, SplashActivity.class);
        intentGame.putExtra("gameToSplash", bundle);
        startActivity(intentGame);
        finish();
    }

    private void unregisterSensor(){
        if (isSensorMood)
            sensorManager.unregisterListener(SEL);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        rockHandler.removeCallbacks(rockCreateRunnable);
        if(sound!=null)
                sound.stop();
        unregisterSensor();
        finish();
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


        //set player IMG
        for (int i = 0; i < main_IMG_playerPos.length; i++) {
            Glide
                    .with(this)
                    .load(R.drawable.car1)
                    .centerInside()
                    .into(main_IMG_playerPos[i]);
        }

    }

    private void setDefaultVisibility() {

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
        int score = thePlayer.getScore();
        main_TXT_score.setText("score:" + score);
    }

    private void moveCar(int desiredPos) {
        int nextPos;
        int currentPos = thePlayer.getPos();

        if (desiredPos == MOVE_LEFT) {
            if (currentPos == 0)
                nextPos = currentPos;
            else
                nextPos = currentPos - 1;
            //MOVE_RIGHT
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
                    updateUiScore();
                    break;
            }
            updateSoundUI(currentItem);
        }
    }

    private void updateSoundUI(FlowingItem currentItem) {
        sound = null;
        switch (currentItem.getITEM_TYPE()) {
            case ROCK:
                sound = MediaPlayer.create(GameActivity.this, R.raw.rock_hit_sound);
                sound.start();
                break;
            case COIN:
                sound = MediaPlayer.create(GameActivity.this, R.raw.coin_sound);
                sound.start();
                break;
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