package com.stars.ojbackendjudgeservice.judge.strategy;


import lombok.Data;
import com.stars.ojbackendmodel.model.model.JudgeInfo;
import com.stars.ojbackendmodel.model.dto.question.JudgeCase;
import com.stars.ojbackendmodel.model.entity.Question;
import com.stars.ojbackendmodel.model.entity.QuestionSubmit;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数
 */

@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private  List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
