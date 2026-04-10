package com.neonates.Enum;

public enum DocumentCategory {
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
