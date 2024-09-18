package com.stars.ojbackendjudgeservice.judge.codesandbox.impl;


import com.stars.ojbackendmodel.model.model.ExecuteCodeRequest;
import com.stars.ojbackendmodel.model.model.ExecuteCodeResponse;
import com.stars.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;

/**
 * 第三方代码沙箱（掉用网上现成的）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");

        return null;
    }
}
