package com.example.spirit.music.tools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LrcHandle {
    private List mWords = new ArrayList();
    private List mTimeList = new ArrayList();
    private static LrcHandle lrcHandle;

    public static LrcHandle getLrcHandle() {
        if (lrcHandle == null) {
            synchronized (LrcHandle.class) {
                if (lrcHandle == null) {
                    lrcHandle = new LrcHandle();
                }
            }
        }
        return lrcHandle;
    }

    /**
     * 处理歌词文件
     */

    public String readLrc(String path) {
        InputStream is = null;
        String text = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                is = connection.getInputStream();
                text = getText(is);
            }

            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getText(InputStream is) {
        int len = -1;
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((len = is.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }

            is.close();
            return baos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
