package com.healthy.library.model;

//推荐返回
public class AppIndexCustomNews {
    public SoundAlbum  album;
    public ActivityInfo  monthlytip;
    public ToolsCEList  notCanEat;
    public NewsInfo  knowledge;
    public ToolsFoodTop eatSuggestInfo;

    public class ActivityInfo {
        public String id;
        public String name;
        public String description;
        public int isDelete;
        public String topicId;
        public String postPkId;
        public String videoIds;
        public String goodsIds;
        public String questionType;
        public String createTime;
        public String updateTime;
        public String img;
        public String postimg;
    }
}
