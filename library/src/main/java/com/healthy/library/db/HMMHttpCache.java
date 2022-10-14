package com.healthy.library.db;

public class HMMHttpCache {
    public String url;
    public String response;
    public String time;

    public HMMHttpCache(String url, String response, String time) {
        this.url = url;
        this.response = response;
        this.time = time;
    }

    @Override
    public String toString() {
        return "HMMHttpCache{" +
                "url='" + url + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
