package com.stars.ojbackendservicclient.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import com.stars.ojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
@FeignClient(name = "oj-backend-judge-service",path = "/api/judge/inner")
public interface JudgeFeginClient {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(long questionSubmitId);
}
