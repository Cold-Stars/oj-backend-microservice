package com.stars.ojbackendjudgeservice.judge;


import cn.hutool.json.JSONUtil;
import com.stars.ojbackendcommon.common.ErrorCode;
import com.stars.ojbackendcommon.exception.BusinessException;
import com.stars.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.stars.ojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.stars.ojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.stars.ojbackendjudgeservice.judge.strategy.JudgeContext;
import com.stars.ojbackendservicclient.service.QuestionFeginClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stars.ojbackendmodel.model.model.ExecuteCodeRequest;
import com.stars.ojbackendmodel.model.model.ExecuteCodeResponse;
import com.stars.ojbackendmodel.model.model.JudgeInfo;
import com.stars.ojbackendmodel.model.dto.question.JudgeCase;
import com.stars.ojbackendmodel.model.entity.Question;
import com.stars.ojbackendmodel.model.entity.QuestionSubmit;
import com.stars.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeginClient questionFeginClient;

    @Resource
    private JudgeManger judgeManger;


    @Value("${codesandbox.type:eaxmple}")
    private String type;

    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {

        // 1、传入题目提交的id，获取对应的题目、提交信息
        QuestionSubmit questionSubmit = questionFeginClient.getQuestionSubmitById(questionSubmitId);

        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交信息不存在");
        }

        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeginClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        // 2、如果不为等待中，就不用重复执行了
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目正在判题中");
        }
        // 3、更改判题状态为判题中、防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());

        boolean update = questionFeginClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }

        // 4、掉用代码沙箱。获取执行结果

        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox  = new CodeSandboxProxy(codeSandbox);
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);


        List<String> outputList = executeCodeResponse.getOutputList();
        // 5.根据执行结果判断判题状态和信息

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        

        JudgeInfo judgeInfo = judgeManger.doJudge(judgeContext);

        // 6 修改数据库判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));

        update = questionFeginClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }

        QuestionSubmit questionSubmitResult = questionFeginClient.getQuestionSubmitById(questionId);

        return questionSubmitResult;
    }
}
