package br.com.pedrovictor.sistlog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackCreateDTO {
    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;

    @NotBlank(message = "Fun fact is required")
    @Size(max = 500, message = "Fun fact must not exceed 500 characters")
    private String funFact;

    @Size(max = 150, message = "Sender must not exceed 150 characters")
    private String sender;

    @Size(max = 150, message = "Client must not exceed 150 characters")
    private String recipient;

    @NotNull(message = "Holiday flag is required")
    private Boolean isHoliday;

    @NotNull(message = "Estimated delivery date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate estimatedDeliveryDate;
}
