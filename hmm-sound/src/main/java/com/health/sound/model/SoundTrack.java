package com.health.sound.model;

public class SoundTrack {
    public long page=1;
    public String pageString="1～20";

    public SoundTrack(long page, String pageString) {
        this.page = page;
        this.pageString = pageString;
    }
}
