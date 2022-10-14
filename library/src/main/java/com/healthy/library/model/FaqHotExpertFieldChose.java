package com.healthy.library.model;

import java.io.Serializable;

public class FaqHotExpertFieldChose implements Serializable {
        public long id;
        public String expertCategoryNo;
        public String expertCategoryName;
        public String countNum;
        public String faceUrl;
        public String iconPath;
        public CountQuestionsResult countQuestionsResults;
        public class CountQuestionsResult{
                public int countQuestions;
        }
}
