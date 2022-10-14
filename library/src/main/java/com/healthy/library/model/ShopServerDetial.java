package com.healthy.library.model;

import java.util.List;

public class ShopServerDetial {

    public List<AmDateInfoListBean> amDateInfoList;
    public List<PmDateInfoListBean> pmDateInfoList;

    public class AmDateInfoListBean {
        public String dateStr;
        public int num;
        public int state;
        public int dateState;
    }

    public class PmDateInfoListBean {


        public String dateStr;
        public int num;
        public int state;
        public int dateState;


    }
}
