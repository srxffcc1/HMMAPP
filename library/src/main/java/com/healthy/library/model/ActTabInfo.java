package com.healthy.library.model;

import com.healthy.library.utils.ResUtil;

public class ActTabInfo {

        public int tabId;

        public String tabTitle;

        public String tabSubTitle;

        public int isFixed;

        public int num;

        public int seqNum;

        public ActTabInfo(String tabTitle, String tabSubTitle,String extra) {
                this.tabTitle = tabTitle;
                this.tabSubTitle = tabSubTitle;
        }
        public String name;

        public int getImageRes() {
                return ResUtil.getInstance().getResourceId(imageResString);
        }

        private Integer imageRes;
        public String imageResString;

        public ActTabInfo(String name, String imageResString) {
                this.name = name;
                this.imageResString = imageResString;
        }
}
