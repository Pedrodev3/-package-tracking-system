package br.com.pedrovictor.sistlog.domain;

import lombok.Getter;

@Getter
public enum PackageStatus {
    CREATED("Created"),
    IN_TRANSIT("In transit"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String label;

    PackageStatus(String label) {
        this.label = label;
    }
}
