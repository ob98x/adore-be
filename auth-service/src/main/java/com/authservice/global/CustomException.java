package com.authservice.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomException extends RuntimeException {

    public CustomException(ResponseCode responseCode) {
        super(responseCode.getMessage()); // 이 부분을 추가하여 메시지를 전달
        this.responseCode = responseCode;
    }

    private final ResponseCode responseCode;
}