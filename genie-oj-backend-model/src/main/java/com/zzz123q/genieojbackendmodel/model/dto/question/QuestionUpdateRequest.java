package com.zzz123q.genieojbackendmodel.model.dto.question;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 题目更新请求
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 题目限制
     */
    private JudgeConfig judgeConfig;

    private static final long serialVersionUID = 1L;
}