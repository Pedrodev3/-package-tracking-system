package br.com.pedrovictor.sistlog.dto.PackDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackCreateRequestDTO {
    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;

    @Size(max = 150, message = "Sender must not exceed 150 characters")
    private String sender;

    @Size(max = 150, message = "Client must not exceed 150 characters")
    private String recipient;

    @NotNull(message = "Estimated delivery date is required")
    @Schema(example = "17/02/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate estimatedDeliveryDate;
}
