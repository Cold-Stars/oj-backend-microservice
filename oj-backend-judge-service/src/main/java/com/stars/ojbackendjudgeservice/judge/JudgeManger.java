package com.stars.ojbackendjudgeservice.judge;


import com.stars.ojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.stars.ojbackendjudgeservice.judge.strategy.JavaJudgeStrategy;
import com.stars.ojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.stars.ojbackendjudgeservice.judge.strategy.JudgeContext;
import org.springframework.stereotype.Service;
import com.stars.ojbackendmodel.model.model.JudgeInfo;
import com.stars.ojbackendmodel.model.entity.QuestionSubmit;

@Service
public class JudgeManger {
    JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();

        String language = questionSubmit.getLanguage();

        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)){
            judgeStrategy = new JavaJudgeStrategy();
        }

        return judgeStrategy.doJudge(judgeContext);
    }
}
