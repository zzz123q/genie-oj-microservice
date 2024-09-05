package com.zzz123q.genieojbackendjudgeservice.judge.strategy;

import com.zzz123q.genieojbackendmodel.model.codesandbox.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     * 
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
