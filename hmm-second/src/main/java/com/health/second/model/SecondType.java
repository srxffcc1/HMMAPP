package com.health.second.model;

import java.util.List;

public class SecondType {
    public SecondType() {

    }

    public int index;

    public String id;
    public String name;
    public List<SecondTypeMenu> secondTypeMenuList;

    public static class SecondTypeMenu {
        public SecondTypeMenu() {

        }

        public SecondTypeMenu(String name, String goodsCategoryIdList) {
            this.name = name;
            this.goodsCategoryIdList = goodsCategoryIdList;
        }

        public SecondTypeMenu(String name) {
            this.name = name;
        }

        public String name;
        public String iconUrl;
        public String ranking;

        public String[] getGoodsCategoryIdList() {
            return goodsCategoryIdList.split(",");
        }

        public String goodsCategoryIdList;
    }
}
