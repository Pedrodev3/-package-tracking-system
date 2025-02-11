package br.com.pedrovictor.sistlog.domain;

public enum PackageStatus {
    CREATED("Created"),
    IN_TRANSIT("In transit"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String label;

    PackageStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
