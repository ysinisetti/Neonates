package com.neonates.Enum;

import lombok.Getter;

@Getter
public enum DocumentCategory {
    GENERAL("general-docs"),
    MEDICAL("medical-docs"),
    FINANCIAL("financial-docs"),
    KYC("kyc-docs");

    private final String containerName;

    DocumentCategory(String containerName) {
        this.containerName = containerName;
    }

    public String getContainerName() {
        return containerName;
    }
}
