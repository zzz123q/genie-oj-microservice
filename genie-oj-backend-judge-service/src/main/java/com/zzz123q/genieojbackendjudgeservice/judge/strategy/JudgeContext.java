package com.zzz123q.genieojbackendjudgeservice.judge.strategy;

import java.util.List;

import com.zzz123q.genieojbackendmodel.model.codesandbox.JudgeInfo;
import com.zzz123q.genieojbackendmodel.model.dto.question.JudgeCase;
import com.zzz123q.genieojbackendmodel.model.dto.question.JudgeConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 判题服务上下文(向判题策略传递参数)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JudgeContext {

    /**
     * 判题用例
     */
    List<JudgeCase> judgeCaseList;

    /**
     * 输入用例
     */
    List<String> inputList;

    /**
     * 输出用例
     */
    List<String> outputList;

    /**
     * 判题信息
     */
    JudgeInfo judgeInfo;

    /**
     * 题目限制
     */
    JudgeConfig judgeConfig;

    /**
     * 编程语言
     */
    String language;
}
