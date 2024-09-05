package com.zzz123q.genieojbackendjudgeservice.judge.strategy;

import java.util.List;

import com.zzz123q.genieojbackendmodel.model.codesandbox.JudgeInfo;
import com.zzz123q.genieojbackendmodel.model.dto.question.JudgeCase;
import com.zzz123q.genieojbackendmodel.model.dto.question.JudgeConfig;
import com.zzz123q.genieojbackendmodel.model.enums.JudgeInfoMessageEnum;

/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     * 
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        JudgeConfig judgeConfig = judgeContext.getJudgeConfig();

        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        JudgeInfo judgeInfoResponse = JudgeInfo.builder()
                .time(time)
                .memory(memory)
                .build();

        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }

        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long timeLimit = judgeConfig.getTimeLimit();
        if (memory > memoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (time > timeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }

}
