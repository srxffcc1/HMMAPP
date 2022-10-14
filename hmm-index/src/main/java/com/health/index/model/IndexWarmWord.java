package com.health.index.model;

public class IndexWarmWord {
        public int type=0;//0为非提醒1为提醒
        public int id;
        public String content;
        public String imageUrl;
        public String hyperLink;
        public String tag;
        public String publisher;
        public String publishDate;
        public int readQuantity;

        public IndexWarmWord(String content) {
                this.content = content;
        }
}
