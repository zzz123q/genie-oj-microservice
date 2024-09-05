package com.zzz123q.genieojbackendmodel.model.dto.questionsubmit;

import java.io.Serializable;
import lombok.Data;

/**
 * 题目提交创建请求
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    private static final long serialVersionUID = 1L;
}