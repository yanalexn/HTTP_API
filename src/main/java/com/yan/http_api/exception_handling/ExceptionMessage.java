package com.yan.http_api.exception_handling;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {
    CLIENT_ID("Клиента с таким id нет в базе"),
    ORDER_ID("There is no such order with this id in the database"),
    NAME("There is no such client with this name in the database");

    private final String message;
}
