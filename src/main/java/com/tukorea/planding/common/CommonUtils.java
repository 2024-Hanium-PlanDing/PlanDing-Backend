package com.tukorea.planding.common;

import com.tukorea.planding.global.error.ErrorResponse;

public class CommonUtils {
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, data, null);
    }

    public static CommonResponse<?> fail(ErrorResponse error) {
        return new CommonResponse<>(false, null, error);
    }

    public static CommonResponse<?> successWithEmptyData() {return new CommonResponse<>(true, null, null);}
}
