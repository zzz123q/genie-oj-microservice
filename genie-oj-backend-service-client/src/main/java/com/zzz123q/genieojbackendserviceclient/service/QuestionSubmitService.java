package com.zzz123q.genieojbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzz123q.genieojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zzz123q.genieojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.zzz123q.genieojbackendmodel.model.entity.QuestionSubmit;
import com.zzz123q.genieojbackendmodel.model.entity.User;
import com.zzz123q.genieojbackendmodel.model.vo.QuestionSubmitVO;

/**
 * 题目提交(question_submit)表服务
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     * 
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    Wrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目提交封装
     *
     * @param questionSubmit
     * @param request
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);

}
