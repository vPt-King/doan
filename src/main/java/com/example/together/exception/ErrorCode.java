package com.example.together.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    INVALID_KEY(1001, "Uncategorized error"),
    USER_EXISTED(1002, "User existed"),
    USERNAME_INVALID(1003, "Username must be at least 3 characters"),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters"),
    USER_NOT_EXISTED(1005, "User not existed"),
    UNAUTHENTICATED(1006, "Unauthenticated"),

    INVALID_OTP(1007,"Invalid otp"),
    OTP_EXPIRED(1008,"OTP Expired"),
    NOT_EQUAL_PASSWORD(1009,"Password and confirm password are not the same"),
    INVALID_USER(1010, "User invalid"),
    INVALID_REQUEST(1011, "Friend request has been sent before"),
    OTP_USED(1012,"has been used"),
    INVALID_GROUPCHAT(1013, "Invalid GroupChat"),
    INVALID_DATA_ACCESS(1014, "Id Null In Repository"),
    FRIENDED(1015,"FRIENDED" ),
    INVALID_NOTIFY(1016,"Invalid Notify");

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}