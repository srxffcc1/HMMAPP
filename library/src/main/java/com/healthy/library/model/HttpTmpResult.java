package com.healthy.library.model;

import java.io.Serializable;

public class HttpTmpResult  implements Serializable {
    public long timeStamp;
    public String result;

    public HttpTmpResult(long timeStamp, String result) {
        this.timeStamp = timeStamp;
        this.result = result;
    }
}
