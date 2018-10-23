package com.example.spirit.music.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.spirit.music.R;
import com.example.spirit.music.Service.MusicService;
import com.example.spirit.music.bean.MusicBean;
import com.example.spirit.music.bean.SongBean;
import com.example.spirit.music.tools.HttpUtil;
import com.example.spirit.music.tools.SearchLocalMusic;

import java.io.InputStream;

public class Fragment03 extends Fragment {

    private ImageView icon;
    private ImageView outLine;
    private ImageView needle;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bm = (Bitmap) msg.obj;

            icon.setImageBitmap(bm);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment03, null);
        initView(view);

        setInfo();
        return view;
    }

    private void setInfo() {
        final SongBean songBean = MusicService.getSongBean();
        if (songBean == null) {
            MusicBean musicBean = MusicService.getMusicBean();
            icon.setImageBitmap(SearchLocalMusic.getSearch().loadBitMapFromMediaStore(getContext
                    (), Long.parseLong(musicBean.getAumbleId())));
        } else {
            if (!TextUtils.isEmpty(songBean.getSonginfo().getPic_premium())) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        InputStream is = HttpUtil.getHttpUtil().getIs(songBean.getSonginfo()
                                .getPic_premium());

                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    }
                }.start();

            } else {
                icon.setImageResource(R.drawable.default_cover);
            }
        }


        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation
                .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(12000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);

        icon.setAnimation(rotateAnimation);

//        outLine.setAnimation(rotateAnimation);
    }

    private void initView(View view) {
        icon = view.findViewById(R.id.rotateIcon);
        outLine = view.findViewById(R.id.outLine);
        needle = view.findViewById(R.id.needle);
    }
}
