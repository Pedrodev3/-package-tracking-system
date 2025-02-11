package br.com.pedrovictor.sistlog.domain;

import lombok.Getter;

@Getter
public enum TrackingEventLocationType {
    CREATED("Centro de Distribuição Osasco", "Pacote registrado e preparado para despacho"),
    IN_TRANSIT("Centro de Distribuição Osasco", "Pacote saiu do centro de distribuição de Osasco e está a caminho para entrega"),
    DELIVERED("Entrega", "Pacote entregue ao destinatário");

    private final String location;
    private final String description;

    TrackingEventLocationType(String location, String description) {
        this.location = location;
        this.description = description;
    }
}
