package com.jiutian.handler.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiutian.handler.BaseErrorInfoInterface;
import com.jiutian.handler.CommonEnum;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;

/**
 * ClassName:ResultBody
 * Package:com.jiutian.pojo
 * Description:
 *
 * @Date: 2021/11/29 23:39
 * @Author: jiutian
 */
@Data
public class ResultBody<T> implements Serializable {
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private T result;

    public ResultBody() {
    }

    public ResultBody(BaseErrorInfoInterface errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }

    /**
     * 成功
     *
     * @return
     */
    public static <T> ResultBody<T> success() {
        return success(null);
    }

    public static <T> ResultBody<T> success(String code, T data) {
        ResultBody<T> rb = new ResultBody<>();
        rb.setCode(code);
        rb.setMessage(CommonEnum.SUCCESS.getResultMsg());
        rb.setResult(data);
        return rb;
    }

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static <T> ResultBody<T> success(T data) {
        ResultBody<T> rb = new ResultBody<>();
        rb.setCode(CommonEnum.SUCCESS.getResultCode());
        rb.setMessage(CommonEnum.SUCCESS.getResultMsg());
        rb.setResult(data);
        return rb;
    }

    /**
     * 失败
     */
    public static <T> ResultBody<T> error(BaseErrorInfoInterface errorInfo) {
        ResultBody<T> rb = new ResultBody<>();
        rb.setCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setResult(null);
        return rb;
    }

    /**
     * 失败
     */
    public static <T> ResultBody<T> error(String code, String message) {
        ResultBody<T> rb = new ResultBody<>();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }

    /**
     * 失败
     */
    public static <T> ResultBody<T> error(String message) {
        ResultBody<T> rb = new ResultBody<>();
        rb.setCode("-1");
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }
}
