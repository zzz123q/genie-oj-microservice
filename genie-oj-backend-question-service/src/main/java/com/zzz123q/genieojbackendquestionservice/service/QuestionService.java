package com.zzz123q.genieojbackendquestionservice.service;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzz123q.genieojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.zzz123q.genieojbackendmodel.model.entity.Question;
import com.zzz123q.genieojbackendmodel.model.vo.QuestionVO;

/**
 * 题目(question)表服务
 */
public interface QuestionService extends IService<Question> {
    /**
     * 校验题目是否合法
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
