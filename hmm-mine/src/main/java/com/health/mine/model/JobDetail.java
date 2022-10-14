package com.health.mine.model;

import java.io.Serializable;
import java.util.List;

public class JobDetail implements Serializable {

    public String jobName;
    public String jobPhone;
    public String jobId;
    public String jobAge;
    public String jobSex;
    public String jobLoc;
    public String jobLocCode;
    public String jobNowLoc;
    public String jobNowLocCode;

    public List<JobArea> adapterLocList;
    public String jobYear;
    public String jobHistory;
    public String jobTeachHistory;
    public String jobGoodAt;
    public List<String> adapterCertPaperList;
    public List<String> adapterHealthPaperList;
    public String jobOtherEt;

}
