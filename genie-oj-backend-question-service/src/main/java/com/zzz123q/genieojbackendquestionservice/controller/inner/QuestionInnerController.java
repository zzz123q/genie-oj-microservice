package com.zzz123q.genieojbackendquestionservice.controller.inner;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zzz123q.genieojbackendmodel.model.entity.Question;
import com.zzz123q.genieojbackendmodel.model.entity.QuestionSubmit;
import com.zzz123q.genieojbackendquestionservice.service.QuestionService;
import com.zzz123q.genieojbackendquestionservice.service.QuestionSubmitService;
import com.zzz123q.genieojbackendserviceclient.service.QuestionFeignClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 该接口仅用于项目内部调用
 */
@RestController
@RequestMapping("/inner")
@Api(tags = "题目内部接口")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 根据id获取题目
     * 
     * @param questionId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    @ApiOperation("根据id获取题目")
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    /**
     * 根据判题完成的题目提交id更新题目统计数据
     * 
     * @param questionSubmitId
     * @return
     */
    @Override
    @PostMapping("/update/id")
    @ApiOperation("根据判题完成的题目提交id更新题目统计数据")
    public boolean updateQuestionBySubmitId(@RequestParam("questionSubmitId") long questionSubmitId) {
        return questionService.updateQuestionBySubmitId(questionSubmitId);
    }

    /**
     * 根据id获取题目提交
     * 
     * @param questionSubmitId
     * @return
     */
    @Override
    @GetMapping("/question_submit/get/id")
    @ApiOperation("根据id获取题目提交")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    /**
     * 更新题目提交信息
     * 
     * @param questionSubmit
     * @return
     */
    @Override
    @PostMapping("question_submit/update")
    @ApiOperation("更新题目提交信息")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}
