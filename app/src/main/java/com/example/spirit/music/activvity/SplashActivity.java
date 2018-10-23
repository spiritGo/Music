package com.example.spirit.music.activvity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.spirit.music.MainActivity;
import com.example.spirit.music.R;
import com.example.spirit.music.tools.HttpClient;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {
    private ImageView flashImage;
    private LinearLayout bottomRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        initView();

        setFlashImage();

        enterMainActivity();
    }

    private void enterMainActivity() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, Main2Activity.class));
                finish();
            }
        }, 3000);
    }

    private void setFlashImage() {
        bottomRight.setVisibility(View.INVISIBLE);
        HttpClient.getHttpClient().showSplash(this, flashImage,bottomRight);
    }

    private void initView() {
        flashImage = findViewById(R.id.flashImage);
        bottomRight = findViewById(R.id.bottomRight);
    }
}
