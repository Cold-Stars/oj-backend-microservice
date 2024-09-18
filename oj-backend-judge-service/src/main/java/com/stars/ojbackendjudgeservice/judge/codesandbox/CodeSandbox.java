package com.stars.ojbackendjudgeservice.judge.codesandbox;


import com.stars.ojbackendmodel.model.model.ExecuteCodeRequest;
import com.stars.ojbackendmodel.model.model.ExecuteCodeResponse;

public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
