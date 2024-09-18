package com.stars.ojbackendservicclient.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.stars.ojbackendmodel.model.entity.Question;
import com.stars.ojbackendmodel.model.entity.QuestionSubmit;

/**
* @author 19731
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-09-02 09:53:28
*/

@FeignClient(name = "oj-backend-question-service",path = "/api/question/inner")
public interface QuestionFeginClient{

    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);


    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId);


    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

}
