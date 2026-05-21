package com.predix.marketschema.dto.response;

import com.predix.marketschema.exception.ErrorCode;
import java.time.OffsetDateTime;

public class ApiResponse<T> {

    private String code;
    private String message;
    private T data;
    private OffsetDateTime timestamp;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.code = ErrorCode.SUCCESS.getCode();
        r.message = ErrorCode.SUCCESS.getDefaultMessage();
        r.data = data;
        r.timestamp = OffsetDateTime.now();
        return r;
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        ApiResponse<T> r = new ApiResponse<>();
        r.code = code;
        r.message = message;
        r.timestamp = OffsetDateTime.now();
        return r;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
