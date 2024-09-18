package com.stars.ojbackendjudgeservice.judge;


import com.stars.ojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */

    QuestionSubmit doJudge(long questionSubmitId);
}
