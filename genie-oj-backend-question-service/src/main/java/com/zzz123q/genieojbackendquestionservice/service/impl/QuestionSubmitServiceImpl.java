package com.zzz123q.genieojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzz123q.genieojbackendcommon.constant.CommonConstant;
import com.zzz123q.genieojbackendcommon.constant.MessageConstant;
import com.zzz123q.genieojbackendcommon.exception.BusinessException;
import com.zzz123q.genieojbackendcommon.result.ErrorCode;
import com.zzz123q.genieojbackendcommon.utils.SqlUtils;
import com.zzz123q.genieojbackendmodel.model.codesandbox.JudgeInfo;
import com.zzz123q.genieojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zzz123q.genieojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.zzz123q.genieojbackendmodel.model.entity.Question;
import com.zzz123q.genieojbackendmodel.model.entity.QuestionSubmit;
import com.zzz123q.genieojbackendmodel.model.entity.User;
import com.zzz123q.genieojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.zzz123q.genieojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.zzz123q.genieojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.zzz123q.genieojbackendmodel.model.vo.QuestionSubmitVO;
import com.zzz123q.genieojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.zzz123q.genieojbackendquestionservice.message.MessageProducer;
import com.zzz123q.genieojbackendquestionservice.service.QuestionService;
import com.zzz123q.genieojbackendquestionservice.service.QuestionSubmitService;
import com.zzz123q.genieojbackendserviceclient.service.JudgeFeignClient;
import com.zzz123q.genieojbackendserviceclient.service.UserFeignClient;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 题目提交(question_submit)表服务实现
 */
@Service
@Slf4j
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;
    @Resource
    private MessageProducer messageProducer;

    /**
     * 题目提交
     * 
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        String language = questionSubmitAddRequest.getLanguage();
        if (QuestionSubmitLanguageEnum.getEnumByValue(language) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }

        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(loginUser.getId());
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean result = this.save(questionSubmit);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交失败");
        }

        Long questionSubmitId = questionSubmit.getId();
        messageProducer.sendMessage(MessageConstant.EXCHANGE_NAME, MessageConstant.ROUTING_KEY,
                String.valueOf(questionSubmitId));

        // 执行判题服务
        // CompletableFuture.runAsync(() -> {
        // QuestionSubmit questionSubmitAfterJudge =
        // judgeFeignClient.doJudge(questionSubmitId);
        // JudgeInfo judgeInfo =
        // JSONUtil.toBean(questionSubmitAfterJudge.getJudgeInfo(), JudgeInfo.class);
        // Question questionUpdate = questionService.getById(questionId);
        // Integer submitNum = questionUpdate.getSubmitNum();
        // Integer acceptedNum = questionUpdate.getAcceptedNum();
        // BigDecimal passingRate = questionUpdate.getPassingRate();
        // submitNum++;
        // if (judgeInfo.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue()))
        // {
        // acceptedNum++;
        // }
        // passingRate = BigDecimal.valueOf((acceptedNum + 0.0) / submitNum);
        // questionUpdate.setSubmitNum(submitNum);
        // questionUpdate.setAcceptedNum(acceptedNum);
        // questionUpdate.setPassingRate(passingRate);
        // boolean update = questionService.updateById(questionUpdate);
        // if (!update) {
        // throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目状态更新失败");
        // }
        // });

        return questionSubmitId;
    }

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    public Wrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();

        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取题目提交封装
     *
     * @param questionSubmit
     * @param request
     * @return
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);

        // 脱敏: (管理员与用户本人可以看见全部信息, 非用户本人只能看到部分公开信息)
        long userId = loginUser.getId();
        if (userId != questionSubmit.getUserId() && !userFeignClient.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }

        return questionSubmitVO;
    }

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage,
            User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(),
                questionSubmitPage.getSize(),
                questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }

        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> {
            return getQuestionSubmitVO(questionSubmit, loginUser);
        }).collect(Collectors.toList());

        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

}