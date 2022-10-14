package com.health.index.model;

import java.util.List;

public class AiQuestionDetail {
    public String id;
    public String problem;
    public String askTitle;
    public List<AnswerBos> answerBos;

    public class AnswerBos{
        public String id;
        public String askId;//提问问题id
        public String serialNum;//提问问题回答选项编号：A、B、C、D
        public String answer;//提问问题回答选项描述（A、B、C、D选项的内容展示)
    }
}
