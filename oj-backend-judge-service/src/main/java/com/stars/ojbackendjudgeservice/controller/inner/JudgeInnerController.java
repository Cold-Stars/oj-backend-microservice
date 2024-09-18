package com.stars.ojbackendjudgeservice.controller.inner;

import com.stars.ojbackendjudgeservice.judge.JudgeService;
import com.stars.ojbackendservicclient.service.JudgeFeginClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stars.ojbackendmodel.model.entity.QuestionSubmit;

import javax.annotation.Resource;

/**
 * 题目接口
 *
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeginClient {

    @Resource
    private JudgeService judgeService;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
