package com.example.spirit.music.activvity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.spirit.music.R;
import com.example.spirit.music.Service.MusicService;
import com.example.spirit.music.fragments.Fragment01;
import com.example.spirit.music.fragments.Fragment02;
import com.example.spirit.music.tools.PlayTool;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mMusic;
    private TextView onLineMusic;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FrameLayout flPage;
    private NavigationView navView;
    private ImageView icon;
    private ImageView play;
    private ImageView next;
    private TextView bottomTitle;
    private TextView bottomArtist;
    private LinearLayout bottomLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        View toolView = View.inflate(this, R.layout.toolbar, null);
        toolbar.setTitle("");
        toolbar.addView(toolView);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initToolBar(toolView);
        initView();

        setFragment();

        play.setImageResource(R.drawable.ic_play_bar_btn_play);
        next.setImageResource(R.drawable.ic_play_bar_btn_next);

        activity = Main2Activity.this;
        startService(new Intent(this, MusicService.class));

        bottomLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, SongInfoActivity.class);
                startActivity(intent);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = PlayTool.getPlayTool().getMediaPlayer();

                if (mediaPlayer.isPlaying()) {
                    play.setImageResource(R.drawable.ic_play_bar_btn_play);
                    mediaPlayer.pause();
                } else {
                    play.setImageResource(R.drawable.ic_play_bar_btn_pause);
                    mediaPlayer.start();
                }
            }
        });
    }

    private static Main2Activity activity;

    public static Main2Activity getActivity() {
        return activity;
    }

    public void setPlayImageResource(int resId) {
        play.setImageResource(resId);
    }

    public void setMusicInfo(Bitmap bitmap, String title, String artist) {
        icon.setImageBitmap(bitmap);
        bottomArtist.setText(artist);
        bottomTitle.setText(title);
    }

    public void setMusicInfo(String url, String title, String artist) {
        Glide.with(getApplicationContext()).load(url).into(icon);
        bottomArtist.setText(artist);
        bottomTitle.setText(title);
    }

    private void setFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flPage, new Fragment01()).commit();

        mMusic.setTextColor(getResources().getColor(R.color.white));
        onLineMusic.setTextColor(getResources().getColor(R.color.grey));

        mMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                mMusic.setTextColor(getResources().getColor(R.color.white));
                onLineMusic.setTextColor(getResources().getColor(R.color.grey));
                onLineMusic.setEnabled(true);
                fragmentManager.beginTransaction().replace(R.id.flPage, new Fragment01()).commit();
            }
        });

        onLineMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                mMusic.setEnabled(true);
                mMusic.setTextColor(getResources().getColor(R.color.grey));
                onLineMusic.setTextColor(getResources().getColor(R.color.white));
                fragmentManager.beginTransaction().replace(R.id.flPage, new Fragment02()).commit();
            }
        });

    }

    private void initToolBar(View toolView) {
        mMusic = toolView.findViewById(R.id.mMusic);
        onLineMusic = toolView.findViewById(R.id.onLineMusic);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.functionSetting) {

        } else if (id == R.id.nightModel) {

        } else if (id == R.id.timer) {

        } else if (id == R.id.exit) {

        } else if (id == R.id.about) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        flPage = findViewById(R.id.flPage);
        navView = findViewById(R.id.nav_view);
        icon = findViewById(R.id.icon);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        bottomTitle = findViewById(R.id.bottomTitle);
        bottomArtist = findViewById(R.id.bottomArtist);
        bottomLL = findViewById(R.id.bottomLL);
    }
}
