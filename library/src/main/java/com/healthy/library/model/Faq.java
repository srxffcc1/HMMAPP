package com.healthy.library.model;

import java.io.Serializable;
import java.util.List;

public class Faq implements Serializable {
    public List<FaqRewardQuestion> rewardQuestionList;
    public List<String> expertPhotoUrls;
    public List<FaqHotExpertField> hotExpertInfoDTOList;
}
