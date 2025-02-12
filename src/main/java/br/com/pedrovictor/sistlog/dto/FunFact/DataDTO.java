package br.com.pedrovictor.sistlog.dto.FunFact;

import lombok.Data;

@Data
public class DataDTO {
    private String id;
    private String type;
    private BodyDTO attributes;
}
