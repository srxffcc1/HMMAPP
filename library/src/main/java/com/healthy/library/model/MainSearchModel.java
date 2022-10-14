package com.healthy.library.model;

import java.io.Serializable;

/**
 * 专题内容
 */
public class MainSearchModel implements Serializable {
        public String id;
        public String keyword;
        public String userId;
        public String createUser;
        public String createTime;

        public MainSearchModel(String keyword) {
                this.keyword = keyword;
        }
}
