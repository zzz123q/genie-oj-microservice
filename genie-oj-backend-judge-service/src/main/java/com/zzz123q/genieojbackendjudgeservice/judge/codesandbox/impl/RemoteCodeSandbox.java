package com.zzz123q.genieojbackendjudgeservice.judge.codesandbox.impl;

import com.zzz123q.genieojbackendcommon.exception.BusinessException;
import com.zzz123q.genieojbackendcommon.result.ErrorCode;
import com.zzz123q.genieojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.zzz123q.genieojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.zzz123q.genieojbackendmodel.model.codesandbox.ExecuteCodeResponse;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

/**
 * 远程代码沙箱(实际调用接口的沙箱)
 */
public class RemoteCodeSandbox implements CodeSandbox {

    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secret";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://192.168.220.129:8082/executeCode";
        HttpResponse httpResponse = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(JSONUtil.toJsonStr(executeCodeRequest))
                .execute();
        String resPonseBodyStr = httpResponse.body();
        if (StrUtil.isBlank(resPonseBodyStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROE, "代码沙箱返回结果为空");
        }
        return JSONUtil.toBean(resPonseBodyStr, ExecuteCodeResponse.class);
    }

}