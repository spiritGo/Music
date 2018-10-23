package com.example.spirit.music.tools;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.spirit.music.interfaces.HttpCallBack;

public class HttpClient {
    private final String SPLASH_URL = "http://guolin.tech/api/bing_pic";
    private static final String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting";
    private static final String METHOD_GET_MUSIC_LIST = "baidu.ting.billboard.billList";
    private static final String METHOD_DOWNLOAD_MUSIC = "baidu.ting.song.play";
    private static final String METHOD_ARTIST_INFO = "baidu.ting.artist.getInfo";
    private static final String METHOD_SEARCH_MUSIC = "baidu.ting.search.catalogSug";
    private static final String METHOD_LRC = "baidu.ting.song.lry";
    private static final String PARAM_METHOD = "method";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_SIZE = "size";
    private static final String PARAM_OFFSET = "offset";
    private static final String PARAM_SONG_ID = "songid";
    private static final String PARAM_TING_UID = "tinguid";
    private static final String PARAM_QUERY = "query";
    private static HttpClient HTTPCLIENT;

    private HttpClient() {
    }

    public static HttpClient getHttpClient() {
        if (HTTPCLIENT == null) {
            synchronized (HttpClient.class) {
                if (HTTPCLIENT == null) {
                    HTTPCLIENT = new HttpClient();
                }
            }
        }
        return HTTPCLIENT;
    }

    public void showSplash(final Activity activity, final ImageView view, final LinearLayout
            bottomRight) {
        HttpUtil.getHttpUtil().getInfo(SPLASH_URL, new HttpCallBack() {
            @Override
            public void info(final String info) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(activity).load(info).into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<?
                                    super GlideDrawable> glideAnimation) {
                                System.out.println("resource:" + resource);
                                view.setImageDrawable(resource);
                                bottomRight.setVisibility(View.VISIBLE);
                            }
                        });
//                        System.out.println("info:" + info);
                    }
                });
            }
        });
    }

    public void getOnLineMusicList(Context context, int type, int size, int offset) {
        String musicListUrl = onLineUrlFormat(METHOD_GET_MUSIC_LIST, type, size, offset);

        System.out.println("musicListUrl:" + musicListUrl);


        HttpUtil.getHttpUtil().getInfo2(context, onLineUrlFormat(musicListUrl, type, size, offset),
                new HttpCallBack() {
                    @Override
                    public void info(String info) {
                        System.out.println("onLineMusic:" + info);
                    }
                });
    }

    public String getOnLineMusicListUrl(Context context, int type, int size, int offset) {
        return onLineUrlFormat(METHOD_GET_MUSIC_LIST, type, size, offset);
    }

    private String onLineUrlFormat(String method, int type, int size, int offSet) {
        return BASE_URL + "?from=webapp_music&method=" + method + "&type=" + String.valueOf(type) +
                "&size=" + String.valueOf(size) + "&offset=" + String.valueOf(offSet);
    }

    public String getSongUrl(long songid) {
        return BASE_URL + "?from=webapp_music&method=" + METHOD_DOWNLOAD_MUSIC + "&songid=" + songid;
    }
}
