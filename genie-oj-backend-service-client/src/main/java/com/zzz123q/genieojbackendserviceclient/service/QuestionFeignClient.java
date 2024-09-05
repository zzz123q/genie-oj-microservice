package com.zzz123q.genieojbackendserviceclient.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.zzz123q.genieojbackendmodel.model.entity.Question;
import com.zzz123q.genieojbackendmodel.model.entity.QuestionSubmit;

/**
 * 题目(question)表服务
 * 提供项目内部调用
 */
@FeignClient(name = "genie-oj-backend-question-service", path = "/oj/question/inner")
public interface QuestionFeignClient {

    /**
     * 根据id获取题目
     * 
     * @param questionId
     * @return
     */
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    /**
     * 根据id获取题目提交
     * 
     * @param questionSubmitId
     * @return
     */
    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    /**
     * 更新题目提交信息
     * 
     * @param questionSubmit
     * @return
     */
    @PostMapping("question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
}
