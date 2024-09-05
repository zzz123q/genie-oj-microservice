package com.zzz123q.genieojbackendjudgeservice.controller.inner;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zzz123q.genieojbackendjudgeservice.judge.JudgeService;
import com.zzz123q.genieojbackendmodel.model.entity.QuestionSubmit;
import com.zzz123q.genieojbackendserviceclient.service.JudgeFeignClient;

/**
 * 该接口仅用于项目内部调用
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    /**
     * 根据题目提交id进行判题服务
     * 
     * @param questionSubmitId
     * @return
     */
    @Override
    @PostMapping("/doJudge")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
