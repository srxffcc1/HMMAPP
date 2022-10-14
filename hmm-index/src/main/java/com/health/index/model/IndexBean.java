package com.health.index.model;

import java.util.List;

public class IndexBean {
    public IndexPregnancyPrepareHomeDataResult pregnancyPrepareHomeDataResult;
    public IndexBabyAndMomChange babyAndMomChangeResult;
    public IndexPostpartumRecovery postpartumRecoveryResult;
    public List<IndexDailyMessageResult> dailyMessageResultList;
    public int status;

    public IndexBean(int status) {
        this.status = status;
    }

    public UserInfoModel memberInfoResult;
    public List<IndexDisCountShop> disCountShops;
    public List<IndexWarmWord> warmWordsList;
    public List<String> banners;
}
