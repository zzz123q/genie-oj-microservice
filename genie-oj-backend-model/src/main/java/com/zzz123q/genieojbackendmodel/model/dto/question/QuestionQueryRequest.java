package com.zzz123q.genieojbackendmodel.model.dto.question;

import java.io.Serializable;
import java.util.List;

import com.zzz123q.genieojbackendcommon.request.PageRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 题目查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {

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
     * 创建用户id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}