package com.stars.ojbackendjudgeservice.judge.codesandbox;


import lombok.extern.slf4j.Slf4j;
import com.stars.ojbackendmodel.model.model.ExecuteCodeRequest;
import com.stars.ojbackendmodel.model.model.ExecuteCodeResponse;

@Slf4j
public class CodeSandboxProxy implements CodeSandbox{

    private final CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox=codeSandbox;
    }
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息："+executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱返回信息："+executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
