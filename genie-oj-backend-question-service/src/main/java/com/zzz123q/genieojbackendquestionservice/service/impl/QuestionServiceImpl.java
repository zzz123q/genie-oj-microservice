package com.zzz123q.genieojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzz123q.genieojbackendcommon.constant.CommonConstant;
import com.zzz123q.genieojbackendcommon.exception.BusinessException;
import com.zzz123q.genieojbackendcommon.exception.ThrowUtils;
import com.zzz123q.genieojbackendcommon.result.ErrorCode;
import com.zzz123q.genieojbackendcommon.utils.SqlUtils;
import com.zzz123q.genieojbackendmodel.model.codesandbox.JudgeInfo;
import com.zzz123q.genieojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.zzz123q.genieojbackendmodel.model.entity.Question;
import com.zzz123q.genieojbackendmodel.model.entity.QuestionSubmit;
import com.zzz123q.genieojbackendmodel.model.entity.User;
import com.zzz123q.genieojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.zzz123q.genieojbackendmodel.model.vo.QuestionVO;
import com.zzz123q.genieojbackendmodel.model.vo.UserVO;
import com.zzz123q.genieojbackendquestionservice.mapper.QuestionMapper;
import com.zzz123q.genieojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.zzz123q.genieojbackendquestionservice.service.QuestionService;
import com.zzz123q.genieojbackendserviceclient.service.UserFeignClient;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 题目(question)表服务实现
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private QuestionSubmitMapper questionSubmitMapper;

    /**
     * 校验题目是否合法
     *
     * @param question
     * @param add
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(tags) && tags.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    /**
     * 获取查询包装类
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }

        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();

        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        // 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userFeignClient.getById(userId);
        }
        UserVO userVO = userFeignClient.getUserVO(user);
        questionVO.setUserVO(userVO);
        return questionVO;
    }

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(),
                questionPage.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUserVO(userFeignClient.getUserVO(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    /**
     * 根据判题完成的题目提交id更新题目统计数据
     * 
     * @param questionSubmitId
     * @return
     */
    @Transactional
    public boolean updateQuestionBySubmitId(long questionSubmitId) {
        QuestionSubmit questionSubmitAfterJudge = questionSubmitMapper.selectById(questionSubmitId);
        JudgeInfo judgeInfo = JSONUtil.toBean(questionSubmitAfterJudge.getJudgeInfo(), JudgeInfo.class);
        Long questionId = questionSubmitAfterJudge.getQuestionId();
        Question questionUpdate = getById(questionId);
        Integer submitNum = questionUpdate.getSubmitNum();
        Integer acceptedNum = questionUpdate.getAcceptedNum();
        BigDecimal passingRate = questionUpdate.getPassingRate();
        submitNum++;
        if (judgeInfo.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue())) {
            acceptedNum++;
        }
        passingRate = BigDecimal.valueOf((acceptedNum + 0.0) / submitNum);
        questionUpdate.setSubmitNum(submitNum);
        questionUpdate.setAcceptedNum(acceptedNum);
        questionUpdate.setPassingRate(passingRate);
        boolean update = updateById(questionUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目状态更新失败");
        }
        return update;
    }
}
