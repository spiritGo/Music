package com.example.spirit.music.tools;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;

import com.example.spirit.music.interfaces.HttpCallBack;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    private OkHttpClient client = new OkHttpClient();
    private static HttpUtil HTTPUTIL;

    public static HttpUtil getHttpUtil() {
        if (HTTPUTIL == null) {
            synchronized (HttpUtil.class) {
                if (HTTPUTIL == null) {
                    HTTPUTIL = new HttpUtil();
                }
            }
        }
        return HTTPUTIL;
    }

    public void getInfo(String url, final HttpCallBack callBack) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String info = response.body().string();

                System.out.println("info:" + info);

                if (callBack != null) {
                    callBack.info(info);
                }
            }
        });
    }

    public void getInfo2(Context context, String url, final HttpCallBack callBack) {
        Request request = new Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", getUserAgent(context))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String info = response.body().string();

                System.out.println("info:" + info);

                if (callBack != null) {
                    callBack.info(info);
                }
            }
        });
    }

    public InputStream getIs(String url){
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            return connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String getUserAgent(Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();

    }
}

