package com.healthy.library.model;

import java.io.Serializable;
import java.util.List;

public class PostImgDetial implements Serializable {

    public String id;
    public String postingId;
    public String url;
    public String annexUrl;
    public int urlType;
    public String createTime;
    public String updateTime;
    public int urlStatus;
    public int sortNum;
    public List<PostingGoodsList> postingGoodsList;
    public List<PostingImgTagList> postingImgTagList;

    public class PostingGoodsList implements Serializable {
        public String id;
        public String postingId;
        public String goodsId;
        public int goodsSource;
        public String goodsUrl;
        public String createTime;
        public String updateTime;
        public int postingImgId;
        public int sortNum;
        public NewGoodsDTOBean newGoodsDTO;

        public class NewGoodsDTOBean implements Serializable {
            public String id;
            public String goodsNo;
            public String barcodeSpu;
            public String goodsName;
            public String goodsTitle;
            public String specName;
            public String specValue;
            public String retailPrice;
            public String storePrice;
            public String platformPrice;
            public int goodsType;
            public String headImage;
            public String categoryName;
            public String brandName;
            public int differentSymbol;
        }
    }

    public class PostingImgTagList implements Serializable {
        public String id;
        public String postingId;
        public String postingImgId;
        public String imgTagName;
        public String pixelCoordinates;
        public String createTime;
        public String updateTime;
    }
}
