package com.healthy.library.model;

import java.io.Serializable;

public class NewsInfo implements Serializable {
        public int id;
        public int categoryId;
        public String stage;
        public String properties;
        public String tag;
        public String title;
        public String summary;
        public String images;
        public String categoryName;
        public String content;
        public String createDate;
        public String updateDate;
        public String publisher;
        public String publishDate;
        public String author;
        public int readQuantity;
        public int fictitiousReadQuantity;
        public int collectionQuantity;
        public int fictitiousCollectionQuantity;
        public int status;
        public int knowOrInfoStatus;
        public int isDelete;
        public int praiseCount;
        public boolean praise;
        public String userSex;
        public String source;
}
