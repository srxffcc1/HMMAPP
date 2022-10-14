package com.healthy.library.model;

public class SoundAlbum {

    public class Announcer {
        public long anchor_grade;

        public String avatar_url;

        public String kind;

        public String nickname;

        public long id;

        public boolean is_verified;
        
    }

    public class Last_uptrack {
        public long duration;

        public long updated_at;

        public long track_id;

        public String track_title;

        public long created_at;
        
    }


    public String album_intro;

    public Announcer announcer;

    public String cover_url_small;

    public long created_at;

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public boolean isStop() {
        return isStop;
    }

    private boolean isStop=false;

    public String recommend_reason;

    public String cover_url_middle;

    public long category_id;

    public long updated_at;

    public long favorite_count;

    public long id;

    public String cover_url_large;

    public String cover_url;

    public String wrap_cover_url;

    public String kind;

    public String short_rich_intro;

    public long play_count;

    public long include_track_count;

    public long is_finished;

    public long share_count;

    public String meta;

    public String album_tags;

    public String selling_point;

    public long subscribe_count;

    public boolean tracks_natural_ordered;

    public Last_uptrack last_uptrack;

    public boolean can_download;

    public String album_title;
    
                

}
