package com.example.spirit.music.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MusicBean implements Serializable {
    private String title;
    private String artist;
    private String aumble;
    private String time;
    private String name;
    private String size;
    private String path;
    private String id;
    private String aumbleId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAumble() {
        return aumble;
    }

    public void setAumble(String aumble) {
        this.aumble = aumble;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAumbleId() {
        return aumbleId;
    }

    public void setAumbleId(String aumbleId) {
        this.aumbleId = aumbleId;
    }

    @Override
    public String toString() {
        return "MusicBean{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", aumble='" + aumble + '\'' +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", path='" + path + '\'' +
                ", id='" + id + '\'' +
                ", aumbleId='" + aumbleId + '\'' +
                '}';
    }

}
