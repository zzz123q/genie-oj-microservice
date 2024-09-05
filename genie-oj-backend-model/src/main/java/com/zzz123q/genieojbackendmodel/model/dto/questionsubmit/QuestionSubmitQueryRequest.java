package com.zzz123q.genieojbackendmodel.model.dto.questionsubmit;

import java.io.Serializable;

import com.zzz123q.genieojbackendcommon.request.PageRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 题目提交查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

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
     * 判题状态(0 - 待判题、1 - 判题中、2 - 成功、3 - 失败)
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}