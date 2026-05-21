package com.predix.marketschema.exception;

public enum ErrorCode {
    SUCCESS("0", "Success"),
    VALIDATION_ERROR("1001", "Validation failed"),
    NOT_FOUND("1002", "Resource not found"),
    INVALID_STATE_TRANSITION("2001", "Invalid market state transition"),
    MARKET_RULE_VIOLATION("2002", "Market business rule violation"),
    OUTCOME_RULE_VIOLATION("2003", "Outcome business rule violation"),
    ORDER_RULE_VIOLATION("2004", "Order business rule violation"),
    IMMUTABLE_MARKET("2005", "Market is immutable in current state"),
    INTERNAL_ERROR("9999", "Internal server error");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
