package com.stars.ojbackendquestionservice.service.controller.inner;

import com.stars.ojbackendquestionservice.service.service.QuestionService;
import com.stars.ojbackendquestionservice.service.service.QuestionSubmitService;
import com.stars.ojbackendservicclient.service.QuestionFeginClient;
import org.springframework.web.bind.annotation.*;
import com.stars.ojbackendmodel.model.entity.Question;
import com.stars.ojbackendmodel.model.entity.QuestionSubmit;

import javax.annotation.Resource;

/**
 * 题目接口
 *
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeginClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") long questionId){
        return questionService.getById(questionId);
    }

    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId){
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("/question_submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit){
        return questionSubmitService.updateById(questionSubmit);
    }


}
