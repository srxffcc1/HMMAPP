package com.healthy.library.model;

import java.util.List;

public class GoodsTran {

    public class GoodsPostageFeeWIthShopDTOS
    {
        public String title;

        public String content;

        @Override
        public String toString() {
            return "GoodsPostageFeeWIthShopDTOS{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
    
        public int isHome;

        public int isExchange;

        public int isSend;

        public List<GoodsPostageFeeWIthShopDTOS> goodsPostageFeeWIthShopDTOS;

    @Override
    public String toString() {
        return "GoodsTran{" +
                "isHome=" + isHome +
                ", isExchange=" + isExchange +
                ", isSend=" + isSend +
                ", goodsPostageFeeWIthShopDTOS=" + goodsPostageFeeWIthShopDTOS +
                '}';
    }
}
