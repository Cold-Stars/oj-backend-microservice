package com.stars.ojbackendjudgeservice.judge.strategy;


import cn.hutool.json.JSONUtil;
import com.stars.ojbackendmodel.model.model.JudgeInfo;
import com.stars.ojbackendmodel.model.dto.question.JudgeCase;
import com.stars.ojbackendmodel.model.dto.question.JudgeConfig;
import com.stars.ojbackendmodel.model.entity.Question;
import com.stars.ojbackendmodel.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {

        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getMemoryLimit();
        Long time = judgeInfo.getTime();

        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();

        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;


        JudgeInfo judgeInfoResponse = new JudgeInfo();

        judgeInfoResponse.setMemoryLimit(memory);
        judgeInfoResponse.setTime(time);

        if (outputList.size()!=inputList.size()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // 依次判断结果
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))){
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        // 判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();

        if (memory>needMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (time>needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
        

    }
}
