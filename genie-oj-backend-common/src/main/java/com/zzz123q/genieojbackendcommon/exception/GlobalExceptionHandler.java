package com.zzz123q.genieojbackendcommon.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zzz123q.genieojbackendcommon.result.BaseResponse;
import com.zzz123q.genieojbackendcommon.result.ErrorCode;
import com.zzz123q.genieojbackendcommon.result.ResultUtils;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        // log.error("BusinessException", e);
        log.info("\n\nBusinessException: {}\n\n", e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        // log.error("RuntimeException", e);
        log.info("\n\nRuntimeException: {}\n\n", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
