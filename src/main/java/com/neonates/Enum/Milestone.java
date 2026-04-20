package com.neonates.Enum;

public enum Milestone {
    M3("3"),
    M6("6"),
    M9("9"),
    M12("12"),
    M18("18"),
    M24("24");

    private final String value;

    Milestone(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
