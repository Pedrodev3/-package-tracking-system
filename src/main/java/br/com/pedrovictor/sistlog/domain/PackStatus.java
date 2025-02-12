package br.com.pedrovictor.sistlog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public boolean canUpdateTO(PackStatus newStatus) {
        switch (this) {
            case CREATED:
                return newStatus == IN_TRANSIT || newStatus == CANCELLED;
            case IN_TRANSIT:
                return newStatus == DELIVERED || newStatus == CANCELLED;
            case DELIVERED:
                return false;
            case CANCELLED:
                return false;
            default:
                return false;
        }
    }
}
