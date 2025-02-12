package br.com.pedrovictor.sistlog.domain;

import lombok.Getter;

@Getter
public enum PackStatus {
    CREATED("Created"),
    IN_TRANSIT("In transit"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String label;

    PackStatus(String label) {
        this.label = label;
    }
}
