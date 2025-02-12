package br.com.pedrovictor.sistlog.domain;

import lombok.Getter;

@Getter
public enum PackStatus {
    CREATED("CREATED"),
    IN_TRANSIT("IN TRANSIT"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED");

    private final String label;

    PackStatus(String label) {
        this.label = label;
    }
}
