package com.zzz123q.genieojbackendjudgeservice.judge;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zzz123q.genieojbackendcommon.exception.BusinessException;
import com.zzz123q.genieojbackendcommon.result.ErrorCode;
import com.zzz123q.genieojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.zzz123q.genieojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.zzz123q.genieojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.zzz123q.genieojbackendjudgeservice.judge.strategy.JudgeContext;
import com.zzz123q.genieojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.zzz123q.genieojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.zzz123q.genieojbackendmodel.model.codesandbox.JudgeInfo;
import com.zzz123q.genieojbackendmodel.model.dto.question.JudgeCase;
import com.zzz123q.genieojbackendmodel.model.dto.question.JudgeConfig;
import com.zzz123q.genieojbackendmodel.model.entity.Question;
import com.zzz123q.genieojbackendmodel.model.entity.QuestionSubmit;
import com.zzz123q.genieojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.zzz123q.genieojbackendserviceclient.service.QuestionService;
import com.zzz123q.genieojbackendserviceclient.service.QuestionSubmitService;

import cn.hutool.json.JSONUtil;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;

    /**
     * 根据题目提交id进行判题服务
     * 
     * @param questionSubmitId
     * @return
     */
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1. 根据题目提交id获取对应的题目、题目提交信息
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }

        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2. 如果题目的状态不为"等待中", 就不用重复执行
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题");
        }
        // 3. 更改提交状态为"判题中", 防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.JUDGING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目提交状态更新失败");
        }

        // 4. 调用沙箱，获取执行结果
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        Long timeLimit = judgeConfig.getTimeLimit();
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .timeLimit(timeLimit)
                .build();

        CodeSandbox box = CodeSandboxFactory.newInstance(type);
        box = new CodeSandboxProxy(box);
        ExecuteCodeResponse response = box.executeCode(executeCodeRequest);

        // 5. 将沙箱执行结果交给相应的判题策略判断
        JudgeContext judgeContext = JudgeContext.builder()
                .judgeCaseList(judgeCaseList)
                .inputList(inputList)
                .outputList(response.getOutputList())
                .judgeConfig(judgeConfig)
                .judgeInfo(response.getJudgeInfo())
                .language(language)
                .build();
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目提交状态更新失败");
        }

        // 6. 返回提交的最新状态
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmitId);
        return questionSubmitResult;
    }

}
