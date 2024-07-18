package com.tukorea.planding.common;

import com.tukorea.planding.global.error.ErrorResponse;
import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private Boolean success;
    private T data;
    private ErrorResponse errorResponse;

    public CommonResponse(Boolean success, T data, ErrorResponse errorResponse) {
        this.success = success;
        this.data = data;
        this.errorResponse = errorResponse;
    }
}
