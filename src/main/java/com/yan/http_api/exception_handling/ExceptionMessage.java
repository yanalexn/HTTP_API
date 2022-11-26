package com.yan.http_api.exception_handling;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {
    CLIENT_ID("Клиента с таким id нет в базе"),
    ORDER_ID("Заказа с таким id нет в базе"),
    NAME("Клиента с таким именем нет в базе");

    private final String message;
}
