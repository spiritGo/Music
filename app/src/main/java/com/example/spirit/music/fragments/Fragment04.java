package com.example.spirit.music.fragments;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spirit.music.R;
import com.example.spirit.music.Service.MusicService;
import com.example.spirit.music.activvity.SongInfoActivity;
import com.example.spirit.music.bean.SongBean;
import com.example.spirit.music.tools.LrcHandle;
import com.example.spirit.music.tools.PlayTool;

import me.wcy.lrcview.LrcView;

public class Fragment04 extends Fragment {

    private LrcView lrcView;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String lrc = (String) msg.obj;
                lrcView.loadLrc(lrc);

                lrcView.setOnPlayClickListener(new LrcView.OnPlayClickListener() {
                    @Override
                    public boolean onPlayClick(long time) {
                        return false;
                    }
                });
                updateLrc();
            } else if (msg.what == 1) {
                updateLrc();
            } else if (msg.what == 2) {
                updateProgress();
            }

        }
    };
    private MediaPlayer player;
    private SongInfoActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment04, null);
        initView(view);

        setLrc();
        return view;
    }

    private void setLrc() {
        SongBean songBean = MusicService.getSongBean();

        if (songBean == null) {
            updateProgress();
        } else {
            final String lrclink = songBean.getSonginfo().getLrclink();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String s = LrcHandle.getLrcHandle().readLrc(lrclink);
                    Log.i(Fragment04.class.getName(), s);
                    Message msg = Message.obtain();
                    msg.obj = s;
                    msg.what = 0;

                    handler.sendMessage(msg);
                }
            }.start();
        }

        activity.setMaxSbBar(player.getDuration());
    }

    private void initView(View view) {
        lrcView = view.findViewById(R.id.lrcView);
        activity = (SongInfoActivity) getActivity();
        player = PlayTool.getPlayTool().getMediaPlayer();
    }

    private void updateLrc() {
        int currentPosition = player.getCurrentPosition();
        lrcView.updateTime(currentPosition);
        if (activity == null) return;
        activity.setSbBar(currentPosition);
        handler.sendEmptyMessageDelayed(1, 300);
    }

    private void updateProgress() {
        if (player.isPlaying()) {
            int currentPosition = player.getCurrentPosition();
            activity.setSbBar(currentPosition);
            handler.sendEmptyMessageDelayed(2, 300);
        }
    }
}
