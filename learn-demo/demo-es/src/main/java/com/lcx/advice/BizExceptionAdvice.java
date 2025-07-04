package com.lcx.advice;

import com.lcx.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : lichangxin
 * @create : 2024/6/26 14:33
 * @description
 */

@ControllerAdvice
@ResponseBody
public class BizExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(BizExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.info("全局异常处理: " + e.getMessage());
        return Result.fail(e.getMessage());
    }


}
