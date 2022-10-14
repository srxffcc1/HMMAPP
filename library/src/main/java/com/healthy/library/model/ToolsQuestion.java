package com.healthy.library.model;

public class ToolsQuestion {
    public String question;
    public String answerA;
    public String answerB;
    public String key;
    public String value="-1";

    public ToolsQuestion(String question, String answerA, String answerB, String key) {
        this.question = question;
        this.answerA = answerA;
        this.answerB = answerB;
        this.key = key;
    }
}
