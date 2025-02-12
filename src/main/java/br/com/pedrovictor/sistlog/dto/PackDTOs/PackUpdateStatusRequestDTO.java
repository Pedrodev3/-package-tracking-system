package br.com.pedrovictor.sistlog.dto.PackDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackUpdateStatusRequestDTO {
    @NotNull(message = "Status is required")
    private String status;
}
