package com.example.CrazyRoadGame.model.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.CrazyRoadGame.MyDB;
import com.example.CrazyRoadGame.R;
import com.example.CrazyRoadGame.model.records.RecordsActivity;


public class SplashActivity extends AppCompatActivity {

    private static final long ANIMATION_DURATION = 4000;
    private ImageView splash_IMG_theImage;
    private TextView splash_TXT_mainTitle;
    private TextView splash_TXT_scoreTitle;

    private MediaPlayer sound;
    private int playerScore;
    private boolean isEnterRT;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bundle = getIntent().getBundleExtra("gameToSplash");
        playerScore = bundle.getInt("score", 0);
        isEnterRT = isEnterRecordTable();

        findViews();
        initViews();

        splash_IMG_theImage.setVisibility(View.INVISIBLE);
        showCarMoving(splash_IMG_theImage);

    }

    private boolean isEnterRecordTable() {
        if(playerScore==0)
            return false;
        if (!MyDB.getInstance().getAllRecords().isEmpty()) {
            if (!(MyDB.getInstance().getAllRecords().size() < MyDB.getInstance().get_MAX_RECORDS()))
                if (MyDB.getInstance().getLowestRecord().getScore() >= playerScore)
                    return false;
        }
        return true;
    }

    private void findViews() {
        splash_IMG_theImage = findViewById(R.id.splash_IMG_theImage);
        splash_TXT_mainTitle = findViewById(R.id.splash_TXT_isEnterRT);
        splash_TXT_scoreTitle = findViewById(R.id.splash_TXT_score);

    }

    private void initViews() {
        if (isEnterRT)
            splash_TXT_mainTitle.setText("It's a new Record!");
        else
            splash_TXT_mainTitle.setText("You loss!");

        splash_TXT_scoreTitle.setText("your score is: " + playerScore);

        // splash_BTN_enter.setOnClickListener();

    }

    private void showCarMoving(final View v) {
        v.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        v.setX(-width / 2);
        v.setScaleX(1.0f);
        v.setScaleY(1.0f);
        v.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .translationX(width)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        sound = null;
                        if (isEnterRT)
                            sound = MediaPlayer.create(SplashActivity.this, R.raw.win_game);
                        else
                            sound = MediaPlayer.create(SplashActivity.this, R.raw.game_over);
                        sound.start();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animationDone();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(sound!=null)
        sound.stop();
        finish();
    }

    private void animationDone() {
        if (isEnterRT) {
            Intent intentRecords = new Intent(this, RecordsActivity.class);
            bundle.putBoolean("isEnterRT", true);
            intentRecords.putExtra("toRecord", bundle);
            this.startActivity(intentRecords);
        }
        finish();
    }
}