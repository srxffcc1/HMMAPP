package com.health.index.model;

import java.util.List;

public class AiResult {
    public String id;
    public String shopChannel;
    public String degree;
    public String temperature;
    public String hospital;
    public String storeNo;
    public String bossPhone;
    public String userPhone;
    public String source;
    public String createDate;
    public String problem;
    public String resultDetail;
    public String advice;
    public String recordId;
    public List<RecordDetailsVos> recordDetailsVos;

    public class RecordDetailsVos{
        public String id;
        public String recordId;
        public String ask;
        public String answer;
    }
}
