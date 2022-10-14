package com.healthy.library.model;

import java.io.Serializable;
import java.util.List;

/**
 * 话题 7014
 */
public class Topic implements Serializable {

    public String id;
    public String topicName;
    public String partInNum;
    public String postingNum;
    public String topicDescription;
    public List<String> faceUrlList;

    public Topic() {
    }

    public Topic(String topicName) {
        this.topicName = topicName;
    }

    public Topic(String id, String topicName) {
        this.id = id;
        this.topicName = topicName;
    }

    public Topic(String id, String topicName, String partInNum, String postingNum, List<String> faceUrlList) {
        this.id = id;
        this.topicName = topicName;
        this.partInNum = partInNum;
        this.postingNum = postingNum;
        this.faceUrlList = faceUrlList;
    }
}
