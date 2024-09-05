package com.zzz123q.genieojbackendserviceclient.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zzz123q.genieojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 * 提供项目内部调用
 */
@FeignClient(name = "genie-oj-backend-judge-service", path = "/oj/judge/inner")
public interface JudgeFeignClient {

    /**
     * 根据题目提交id进行判题服务
     * 
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/doJudge")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
