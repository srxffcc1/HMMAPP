package com.healthy.library.model;

import java.io.Serializable;

public class PostTop implements Serializable {
    public long id;
    public String postingContent;
    public String postingTag;
    public String postingTitle;
    public int topStatus=1;
    public int postingType;
}
