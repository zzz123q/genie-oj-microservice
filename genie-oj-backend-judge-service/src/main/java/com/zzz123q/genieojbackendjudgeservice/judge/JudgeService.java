package com.zzz123q.genieojbackendjudgeservice.judge;

import com.zzz123q.genieojbackendmodel.model.entity.QuestionSubmit;

public interface JudgeService {

    /**
     * 根据题目提交id进行判题服务
     * 
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
