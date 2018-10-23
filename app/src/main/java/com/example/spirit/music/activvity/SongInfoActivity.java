package com.example.spirit.music.activvity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.spirit.music.R;
import com.example.spirit.music.Service.MusicService;
import com.example.spirit.music.bean.MusicBean;
import com.example.spirit.music.bean.SongBean;
import com.example.spirit.music.fragments.Fragment03;
import com.example.spirit.music.fragments.Fragment04;

import java.util.ArrayList;

public class SongInfoActivity extends FragmentActivity {
    private TextView musicTitle;
    private TextView musicArtist;
    private ViewPager vp;
    private LinearLayout dotLL;
    private SeekBar sbBar;
    private ImageView prev;
    private ImageView play;
    private ImageView next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songinfo_activity);

        initView();

        setViewPager();

        setinfo();
    }

    private void setinfo() {
        SongBean songBean = MusicService.getSongBean();
        if (songBean == null) {
            MusicBean musicBean = MusicService.getMusicBean();
            musicTitle.setText(musicBean.getTitle());
            musicArtist.setText(musicBean.getArtist());
        } else {
            musicTitle.setText(songBean.getSonginfo().getTitle());
            musicArtist.setText(songBean.getSonginfo().getAuthor());
        }

        play.setImageResource(R.drawable.ic_play_btn_pause);
    }

    private void setViewPager() {

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fragment03());
        fragments.add(new Fragment04());
        vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fragments));

    }

    private void initView() {
        musicTitle = findViewById(R.id.musicTitle);
        musicArtist = findViewById(R.id.musicArtist);
        vp = findViewById(R.id.vp);
        dotLL = findViewById(R.id.dotLL);
        sbBar = findViewById(R.id.sb_bar);
        prev = findViewById(R.id.prev);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
    }

    public void setSbBar(int progress) {
        sbBar.setProgress(progress);
    }

    public void setMaxSbBar(int max) {
        sbBar.setMax(max);
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;

        MyViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
