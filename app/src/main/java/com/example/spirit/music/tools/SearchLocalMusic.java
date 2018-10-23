package com.example.spirit.music.tools;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.example.spirit.music.R;
import com.example.spirit.music.bean.MusicBean;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class SearchLocalMusic {

    private static SearchLocalMusic searchLocalMusic;
    private Bitmap bm;

    private SearchLocalMusic() {}

    public static SearchLocalMusic getSearch() {
        if (searchLocalMusic == null) {
            synchronized (SearchLocalMusic.class) {
                if (searchLocalMusic == null) {
                    searchLocalMusic = new SearchLocalMusic();
                }
            }
        }

        return searchLocalMusic;
    }

    public ArrayList<MusicBean> search(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);
        if (cursor == null) return null;
        ArrayList<MusicBean> musicBeans = new ArrayList<>();
        while (cursor.moveToNext()) {
            MusicBean musicBean = new MusicBean();
            musicBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media
                    .TITLE)));
            musicBean.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media
                    .ARTIST)));
            musicBean.setAumble(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media
                    .ALBUM)));
            musicBean.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media
                    .DISPLAY_NAME)));
            musicBean.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            musicBean.setSize(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
            musicBean.setTime(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media
                    .DURATION)));
            musicBean.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            musicBean.setAumbleId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media
                    .ALBUM_ID)));
            musicBeans.add(musicBean);
        }
        cursor.close();
        if (musicBeans.size() == 0) return musicBeans;
        return musicBeans;
    }

    private Uri getsArtworkUri(long albumId) {
        Uri artWorkUri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(artWorkUri, albumId);
    }

    public Bitmap loadBitMapFromMediaStore(Context context, long albumId) {
        ContentResolver resolver = context.getContentResolver();
        InputStream is;
        try {
            is = resolver.openInputStream(getsArtworkUri(albumId));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.default_cover);
        }

        return Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), desity.dp2px(context,
                50), desity.dp2px(context, 50), true);
    }

    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

    public Bitmap getArtWorkFormFile(Context context, long songId, long albumId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        FileDescriptor fd = null;

        try {
            if (albumId < 0) {

                Uri uri = Uri.parse("content://media/external/audio/media/" + songId + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri,
                        "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor
                        (uri, "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            }

            options.inSampleSize = 1;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            options.inSampleSize = 50;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.default_cover);
        }

        return bm;
    }

}
