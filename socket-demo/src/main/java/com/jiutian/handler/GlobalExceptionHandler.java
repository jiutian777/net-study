package com.jiutian.handler;

import com.jiutian.error.MyException;
import com.jiutian.handler.api.ResultBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * ClassName:GlobalExceptionHandler
 * Package:com.jiutian.handler
 * Description:
 *
 * @Date: 2021/12/11 11:01
 * @Author: jiutian
 */
@ControllerAdvice
public class GlobalExceptionHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ResultBody<T> myExceptionHandler(HttpServletRequest req, MyException e) {
        logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultBody.error(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultBody<T> exceptionHandler(HttpServletRequest req, NullPointerException e) {
        logger.error("发生空指针异常！原因是:", e);
        return ResultBody.error(CommonEnum.BODY_NOT_MATCH);
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseBody
    public ResultBody<T> exceptionHandler(HttpServletRequest req, DuplicateKeyException e) {
        logger.error("发生重复键值异常！原因是:", e);
        return ResultBody.error("键值不能重复");
    }

    @ExceptionHandler(value = IOException.class)
    @ResponseBody
    public ResultBody<T> exceptionHandler(HttpServletRequest req, IOException e) {
        logger.error("文件写入异常！原因是:", e);
        return ResultBody.error("文件写入异常");
    }

    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultBody<T> exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("未知异常！原因是:", e);
        return ResultBody.error(CommonEnum.INTERNAL_SERVER_ERROR);
    }
}
