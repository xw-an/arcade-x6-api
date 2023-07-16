package com.x6.arcade.advice;

import com.x6.arcade.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public CommonResponse<String> handleCommerceException (HttpServletRequest req, Exception ex) {
        log.error("commerce service has error: [{}]", ex.getMessage(), ex);
        return new CommonResponse<>(-1,ex.getMessage(),"business error");
    }
}
