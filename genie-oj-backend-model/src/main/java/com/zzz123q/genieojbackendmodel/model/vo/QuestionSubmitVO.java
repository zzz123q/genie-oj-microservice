package com.zzz123q.genieojbackendmodel.model.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.zzz123q.genieojbackendmodel.model.codesandbox.JudgeInfo;
import com.zzz123q.genieojbackendmodel.model.entity.QuestionSubmit;

import cn.hutool.json.JSONUtil;
import lombok.Data;

/**
 * 题目提交视图
 */
@Data
public class QuestionSubmitVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息(json对象)
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态(0 - 待判题、1 - 判题中、2 - 成功、3 - 失败)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param questionSubmitVO
     * @return
     */
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);
        JudgeInfo judgeInfo = questionSubmitVO.getJudgeInfo();
        if (judgeInfo != null) {
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        }
        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        questionSubmitVO.setJudgeInfo(JSONUtil.toBean(questionSubmit.getJudgeInfo(), JudgeInfo.class));
        return questionSubmitVO;
    }
}