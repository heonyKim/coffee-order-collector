package com.heony.coffee_order_collector._common.exception;

public enum ErrorCodes {

    NAME_USED_ALREADY(400, "이미 사용중인 이름 입니다."),
    UPDATE_AVAILABLE_TIMEOUT(400, "변경 가능한 시간이 지났습니다."),
    UPDATE_AVALIABLE_DISABLE(400, "변경이 불가능 합니다."),
    MENU_BLACK_USED_ALREADY(400, "이미 등록된 주문불가 메뉴 입니다."),
    MENU_BLACK_LIST(400, "주문이 불가능한 메뉴입니다."),

    NOT_FOUND_PAGE(404, "존재하지 않는 페이지 입니다."),
    MEMBER_NOT_FOUND(404, "존재하지 않는 회원입니다."),
    ;

    private final int httpStatusCode;
    private final String message;

    public static ErrorCodes getByName(String name) {
        for (ErrorCodes errorCode : ErrorCodes.values()) {
            if (errorCode.name().equals(name)) {
                return errorCode;
            }
        }
        return null;
    }

    ErrorCodes(int httpStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public int httpStatusCode() {
        return httpStatusCode;
    }

    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
