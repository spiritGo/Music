package com.example.spirit.music.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.spirit.music.activvity.Main2Activity;
import com.example.spirit.music.bean.MusicBean;
import com.example.spirit.music.bean.SongBean;
import com.example.spirit.music.interfaces.HttpCallBack;
import com.example.spirit.music.tools.HttpUtil;
import com.example.spirit.music.tools.PlayTool;
import com.example.spirit.music.tools.SearchLocalMusic;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MusicService extends Service {

    private ArrayList<MusicBean> musicBeans;
    private MusicBroadCast musicBroadCast;
    private static MusicBean musicBean;
    private static Context context;

    @SuppressLint("HandlerLeak")
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String url = (String) msg.obj;
//            System.out.println("songUrl:" + url);

            HttpUtil.getHttpUtil().getInfo2(context, url, new HttpCallBack() {
                @Override
                public void info(String info) {
                    try {
                        songBean = new Gson().fromJson(info, SongBean.class);
                        System.out.println("file_link:" + songBean.getBitrate().getFile_link());

                        PlayTool.getPlayTool().onlinePlay(songBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private static SongBean songBean;
    private int position;


    public static SongBean getSongBean() {
        return songBean;
    }

    public static MusicBean getMusicBean() {
        return musicBean;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicBroadCast = new MusicBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("play");
        filter.addAction("online_play");
        filter.addAction("next");
        registerReceiver(musicBroadCast, filter);

        musicBeans = SearchLocalMusic.getSearch().search(getApplicationContext());
        position = getSharedPreferences("music", MODE_PRIVATE).getInt("position", 0);
        musicBean = musicBeans.get(position);

        if (musicBean != null) {

            if (Main2Activity.getActivity() == null) return;
            Main2Activity.getActivity().setMusicInfo(SearchLocalMusic.getSearch()
                    .loadBitMapFromMediaStore(getApplicationContext(), Long.parseLong(musicBean
                            .getAumbleId())), musicBean.getTitle(), musicBean.getArtist());
        }

        context = getApplicationContext();
        next();
    }

    private void next() {
        MediaPlayer mediaPlayer = PlayTool.getPlayTool().getMediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextMusic();
            }
        });
    }

    private void nextMusic() {
        if (position <= musicBeans.size() - 1) {
            PlayTool.getPlayTool().start(musicBeans.get(position));
            position++;
        }
    }

    public static class MusicBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("play".equals(action)) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) return;
                musicBean = (MusicBean) bundle.getSerializable("musicBean");
                PlayTool.getPlayTool().start(musicBean);
                songBean = null;
                System.out.println("hello, I come here!");
            } else if ("online_play".equals(action)) {
                String songUrl = intent.getStringExtra("songUrl");
                Message obtain = Message.obtain();
                obtain.obj = songUrl;
                handler.sendMessage(obtain);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(musicBroadCast);
    }
}
