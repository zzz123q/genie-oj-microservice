package com.zzz123q.genieojbackendjudgeservice.judge.codesandbox.impl;

import com.zzz123q.genieojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.zzz123q.genieojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.zzz123q.genieojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱(调用第三方代码沙箱完成判题工作)
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }

}