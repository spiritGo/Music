package com.example.spirit.music.tools;

import android.media.MediaPlayer;

import com.example.spirit.music.R;
import com.example.spirit.music.activvity.Main2Activity;
import com.example.spirit.music.bean.MusicBean;
import com.example.spirit.music.bean.SongBean;

import java.io.IOException;

public class PlayTool {

    private static PlayTool playTool;
    private final MediaPlayer mediaPlayer;
    private final Main2Activity activity;

    private PlayTool() {
        mediaPlayer = new MediaPlayer();
        activity = Main2Activity.getActivity();
    }

    public static PlayTool getPlayTool() {
        if (playTool == null) {
            synchronized (PlayTool.class) {
                if (playTool == null) {
                    playTool = new PlayTool();
                }
            }
        }
        return playTool;
    }

    public void start(MusicBean musicBean) {
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            activity.setPlayImageResource(R.drawable.ic_play_bar_btn_pause);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onlinePlay(SongBean songBean) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songBean.getBitrate().getFile_link());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            activity.setPlayImageResource(R.drawable.ic_play_bar_btn_pause);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
