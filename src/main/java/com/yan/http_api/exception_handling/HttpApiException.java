package com.yan.http_api.exception_handling;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpApiException extends RuntimeException{

    private String message;
}
