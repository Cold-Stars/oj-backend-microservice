package com.stars.ojbackendjudgeservice.judge.strategy;


import com.stars.ojbackendmodel.model.model.JudgeInfo;

/**
 * 叛徒策略
 */
public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext judgeContext);
}
