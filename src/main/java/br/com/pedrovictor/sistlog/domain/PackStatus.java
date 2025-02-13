package br.com.pedrovictor.sistlog.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PackStatus {
    CREATED("CREATED", "criado"),
    IN_TRANSIT("IN TRANSIT", "em trânsito"),
    DELIVERED("DELIVERED", "entregue"),
    CANCELLED("CANCELLED", "cancelado");

    private final String label;
    private final String labelPtBr;

    PackStatus(String label, String labelPtBr) {
        this.label = label;
        this.labelPtBr = labelPtBr;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static PackStatus fromLabel(String label) {
        return Arrays.stream(PackStatus.values())
                .filter(status -> status.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid status: " + label +
                                ". Allowed values: " + Arrays.toString(PackStatus.values())));
    }

    public boolean canUpdateTO(PackStatus newStatus) {
        return switch (this) {
            case CREATED -> newStatus == IN_TRANSIT || newStatus == CANCELLED;
            case IN_TRANSIT -> newStatus == DELIVERED;
            case DELIVERED -> false;
            case CANCELLED -> false;
        };
    }
}
