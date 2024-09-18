package com.stars.ojbackendjudgeservice.judge.codesandbox.impl;


import com.stars.ojbackendmodel.model.model.ExecuteCodeRequest;
import com.stars.ojbackendmodel.model.model.ExecuteCodeResponse;
import com.stars.ojbackendmodel.model.model.JudgeInfo;
import com.stars.ojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.stars.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.stars.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;

import java.util.List;

/**
 * 示例代码沙箱
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();



        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemoryLimit(100L);
        judgeInfo.setTime(100L);

        executeCodeResponse.setJudgeInfo(judgeInfo);
        
        return executeCodeResponse ;
    }
}
